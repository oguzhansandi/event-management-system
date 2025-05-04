package com.event.services;

import com.event.dto.RegistrationResponse;

import java.util.List;

public interface IRegistrationsService {

    public RegistrationResponse joinToEvent(Long eventID);

    public List<RegistrationResponse> getRegistirationEvents();
}
