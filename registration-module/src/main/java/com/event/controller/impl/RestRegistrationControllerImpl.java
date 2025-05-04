package com.event.controller.impl;

import com.event.controller.IRestRegistrationController;
import com.event.controller.RootEntity;
import com.event.dto.RegistrationResponse;
import com.event.services.IRegistrationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api")
public class RestRegistrationControllerImpl implements IRestRegistrationController {

    @Autowired
    private IRegistrationsService registrationsService;

    @PostMapping("/registrations/{eventID}")
    @Override
    public RootEntity<RegistrationResponse> joinToEvent(@PathVariable("eventID") Long eventID) {
        return RootEntity.ok(registrationsService.joinToEvent(eventID));
    }

    @GetMapping("/registrations")
    @Override
    public RootEntity<List<RegistrationResponse>> getRegistirationEvents() {
        return RootEntity.ok(registrationsService.getRegistirationEvents());
    }
}
