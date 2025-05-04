package com.event.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoUser {

    @NotEmpty
    private String username;

    @NotEmpty
    private String mail;

    @NotEmpty
    private String password;
}
