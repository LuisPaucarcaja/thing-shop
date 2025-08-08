package com.microservice_auth.feature.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 8)
    private String password;

}
