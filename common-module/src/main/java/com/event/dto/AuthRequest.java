package com.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest extends DtoBase {

    private String username;

    private String mail;

    private String password;
}
