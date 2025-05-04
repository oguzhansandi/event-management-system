package com.event.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RegistrationResponse {

    private Long id;

    private Date registrationDate;

    private String user;

    private String event;
}
