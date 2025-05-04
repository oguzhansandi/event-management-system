package com.event.controller;

import com.event.dto.RegistrationResponse;

import java.util.List;

public interface IRestRegistrationController {
    public RootEntity<RegistrationResponse> joinToEvent(Long eventID);

    public RootEntity<List<RegistrationResponse>> getRegistirationEvents();
}
