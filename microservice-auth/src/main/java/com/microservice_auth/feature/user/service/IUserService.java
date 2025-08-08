package com.microservice_auth.feature.user.service;


import com.microservice_auth.feature.user.dto.UserDto;
import com.microservice_auth.feature.user.entity.Role;
import com.microservice_auth.feature.user.entity.User;
import com.microservice_auth.feature.user.enums.AccountStatus;
import com.microservice_auth.feature.user.dto.CreateUserRequest;
import com.microservice_auth.feature.user.dto.UpdateUserRequest;

public interface IUserService {

    UserDto getUserById(Long userId);
    UserDto createUser(CreateUserRequest userRequest);

    UserDto updateUser(Long userId, UpdateUserRequest userRequest);

    User getUserByEmail(String email);
    void updateUserAccountStatus(Long userId, AccountStatus accountStatus);
    Role getDefaultUserRole();


}
