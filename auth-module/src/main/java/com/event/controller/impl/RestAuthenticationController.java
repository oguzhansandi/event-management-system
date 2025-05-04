package com.event.controller.impl;

import com.event.controller.IRestAuthenticationController;
import com.event.controller.RootEntity;
import com.event.dto.AuthRequest;
import com.event.dto.AuthResponse;
import com.event.dto.DtoUser;
import com.event.services.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestAuthenticationController implements IRestAuthenticationController {

    @Autowired
    private IAuthenticationService authenticationService;

    @PostMapping("/register")
    @Override
    public RootEntity<DtoUser> register(@RequestBody AuthRequest input) {
        return RootEntity.ok(authenticationService.register(input));
    }

    @PostMapping("/authenticate")
    @Override
    public RootEntity<AuthResponse> authenticate(@RequestBody AuthRequest input) {
        return RootEntity.ok(authenticationService.authenticate(input));
    }

    @DeleteMapping("/user")
    @Override
    public RootEntity<String> deleteAccount() {
        return RootEntity.ok(authenticationService.deleteAccount());
    }
}
