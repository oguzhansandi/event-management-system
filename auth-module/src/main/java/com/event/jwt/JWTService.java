package com.event.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTService {

    public static final String SECRET_KEY= "790aaf0fa42e7558cfde775d6dba1b14c5584dc0f32213cf0d6020d7e089911a";

    //Token üretimi
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) //kullanıcı adı
                .setIssuedAt(new Date()) //oluşturulma tarihi
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*2)) // şuanki zamanı alıyoruz ve burada 2 saaat
                .signWith(getKey(), SignatureAlgorithm.HS256) // SignWith ile güveli, HS256 imza
                .compact();
    }

    /* Burada key'i Base64 formatında çözüp, güvenli bir key yapıyoruz.
        Token doğrulamada kullanılıyor.
    */
    public Key getKey(){
        byte[] bytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }

    /*
    * Tokendaki bilgileri alabilmek için bu fonksiyon yazıldı*/
    public <T> T exportToken(String token, Function<Claims, T> claimsFunc){
        Claims claims = getClaims(token);
        return claimsFunc.apply(claims);
    }

    /*
    * Token geçerliliği(Şuanki zaman token bitiş tarihinden önce mi?)
    * */
    public boolean isTokenValid(String token) {
        Date expireDate = exportToken(token, Claims::getExpiration);
        return new Date().before(expireDate);
    }

    //Token kullanıcı adı export fonksiyonu yardımıyla
    public String getUsernameByToken(String token){
        return exportToken(token, Claims::getSubject);
    }

    //Token içeriğini parse ediyoruz
    public Claims getClaims(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }
}
