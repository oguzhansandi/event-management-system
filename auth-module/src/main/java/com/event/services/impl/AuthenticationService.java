package com.event.services.impl;

import com.event.dto.AuthRequest;
import com.event.dto.AuthResponse;
import com.event.dto.DtoUser;
import com.event.exception.BaseException;
import com.event.exception.ErrorMessage;
import com.event.exception.MessageType;
import com.event.jwt.JWTService;
import com.event.model.RefreshToken;
import com.event.model.User;
import com.event.repository.RefreshTokenRepository;
import com.event.repository.UserRepository;
import com.event.services.CommonServices;
import com.event.services.IAuthenticationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationService implements IAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private CommonServices commonServices;

    @Autowired
    private JWTService jwtService;

    private User createUser(AuthRequest input){
        User user = new User();
        user.setUsername(input.getUsername());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setMail(input.getMail());
        user.setCreateDate(new Date());
        return user;
    }

    @Override
    public DtoUser register(AuthRequest input) {
        try {
            DtoUser dtoUser = new DtoUser();
            if (userRepository.findByUsername(input.getUsername()).isPresent()) {
                throw new BaseException(new ErrorMessage(MessageType.USER_ALREADY_EXISTS, "username: " + input.getUsername()));
            }

            if (userRepository.findByMail(input.getMail()).isPresent()) {
                throw new BaseException(new ErrorMessage(MessageType.USER_ALREADY_EXISTS, "mail: " + input.getMail()));
            }

            User savedUser = userRepository.save(createUser(input));
            BeanUtils.copyProperties(savedUser, dtoUser);
            return dtoUser;
        }catch (Exception e){
            throw new BaseException(new ErrorMessage(MessageType.REGISTER_FAILED, e.getMessage()));
        }
    }

    private RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken =new RefreshToken();
        refreshToken.setCreateDate(new Date());
        refreshToken.setExpiredDate(new Date(System.currentTimeMillis() + 1000*60*60*4));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        return refreshToken;
    }

    @Override
    public AuthResponse authenticate(AuthRequest input) {
        try {

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword());
            authenticationProvider.authenticate(authenticationToken);
            Optional<User> optUser = userRepository.findByUsername(input.getUsername());

            String accessToken = jwtService.generateToken(optUser.get());

            RefreshToken savedRefreshToken = refreshTokenRepository.save(createRefreshToken(optUser.get()));

            return new AuthResponse(input.getUsername(),accessToken, savedRefreshToken.getRefreshToken());
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Authentication error: " + e.getMessage());
            throw new BaseException(new ErrorMessage(MessageType.AUTHENTICATION_FAILED, e.getMessage()));
        }
    }

    @Override
    @Transactional
    public String deleteAccount() {
        String username = commonServices.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BaseException(
                        new ErrorMessage(MessageType.USER_NOT_FOUND, "Kullanıcı adı : " + username)
                ));
        refreshTokenRepository.deleteAllByUser(user);
        userRepository.delete(user);
        return "Kullanıcı başarıyla silindi";
    }
}
