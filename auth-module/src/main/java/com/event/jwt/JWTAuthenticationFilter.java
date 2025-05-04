package com.event.jwt;

import com.event.exception.BaseException;
import com.event.exception.ErrorMessage;
import com.event.exception.MessageType;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//HTTP'den gelen isteğin JWT kontrolü burada yapılır
@Component
public class JWTAuthenticationFilter  extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // Eğer Auth başlığı yoksa yani token yoksa doFilter ile süreci bitir
        if (header == null){
            filterChain.doFilter(request,response);
            return;
        }

        String token;

        token = header.substring(7);
        try{
            String username = jwtService.getUsernameByToken(token);
            // eğer username ? true ve henüz auth olmadıysa
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                //kullanıcı bilgilerin getir
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //Kullanıcı varsa ve token geçerliyse?
                if(userDetails !=null && jwtService.isTokenValid(token)){
                   //Kimlik doğrulama=> Bu kişi şu yetkilere sahip
                    UsernamePasswordAuthenticationToken authenticationToken = new
                            UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());

                    /*IP,SessionID bilgilerini alıyoruz ki hangi ip'den ne istek geliyor?
                      Güvenlik açısından önemli
                     * */
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    //User giriş yaptı
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }catch (ExpiredJwtException ex){
            throw  new BaseException(new ErrorMessage(MessageType.TOKEN_IS_EXPIRED, ex.getMessage()));
        }catch (Exception e){
            throw new BaseException(new ErrorMessage(MessageType.GENERAL_EXCEPTION, e.getMessage()));
        }

        //filtre doğru çalıştı sıradaki filtreye veya controllera geç
        filterChain.doFilter(request, response);
    }
}
