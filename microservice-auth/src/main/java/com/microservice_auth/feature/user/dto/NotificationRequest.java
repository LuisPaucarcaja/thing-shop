package com.microservice_auth.feature.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotificationRequest {

    private String email;
    private String token;
}
