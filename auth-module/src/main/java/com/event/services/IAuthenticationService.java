package com.event.services;

import com.event.dto.AuthRequest;
import com.event.dto.AuthResponse;
import com.event.dto.DtoUser;

public interface IAuthenticationService {

    public DtoUser register(AuthRequest input);

    public AuthResponse authenticate(AuthRequest input);

    public String deleteAccount();
}
