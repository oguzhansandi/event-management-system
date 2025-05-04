package com.event.config;

import com.event.jwt.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

//korumalı enpointler, jwt gerekli istekler
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JWTAuthenticationFilter authenticationFilter;

    @Value("${frontend.url}")
    private String localhost;

    public static final String REGISTER = "/register";
    public static final String AUTHENTICATE = "/authenticate";
    public static final String REFRESH_TOKEN = "/refreshToken";
    public static final String INDEX = "/";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request->
                        request.requestMatchers(REGISTER, REFRESH_TOKEN,AUTHENTICATE,INDEX).permitAll()
                                .anyRequest()
                                .authenticated())
                //Her istek jwt tokenle gelmeli
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                //eğer token varsa kullanıcıyı tanır
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(c -> c.configurationSource(corsConfigurationSource()));
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(localhost));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
