package com.event.controller;

import com.event.dto.AuthRequest;
import com.event.dto.AuthResponse;
import com.event.dto.DtoUser;

public interface IRestAuthenticationController {

    public RootEntity<DtoUser> register(AuthRequest input);

    public RootEntity<AuthResponse> authenticate(AuthRequest input);

    public RootEntity<String> deleteAccount();
}
