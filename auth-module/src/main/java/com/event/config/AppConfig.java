package com.event.config;

import com.event.exception.BaseException;
import com.event.exception.ErrorMessage;
import com.event.exception.MessageType;
import com.event.model.User;
import com.event.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Configuration
public class AppConfig {

    @Autowired
    private UserRepository userRepository;

    // Bu bean ile varsa kullanıcı getirdik.
        @Bean
        UserDetailsService userDetailsService() {
            return new UserDetailsService() {
                @Override
                public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                    Optional<User> optUser = userRepository.findByUsername(username);
                    if (optUser.isEmpty()) {
                        throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, username));
                    }
                    return optUser.get(); // User sınıfın UserDetails implement ediyor ya, sorun yok
                }
            };
        }


        //Burada gelen(username,şifre vs.) gibi bilgilerin doğrulu?
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    //Şifre kriptolama
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    //Kimlik doğrulama
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

}
