package com.microservice_auth.feature.user.dto;


import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto{


    private String email;
    private String fullName;
    private String phoneNumber;
    private String avatarUrl;

}
