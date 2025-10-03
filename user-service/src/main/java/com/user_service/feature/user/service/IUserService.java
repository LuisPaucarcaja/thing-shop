package com.user_service.feature.user.service;


import com.user_service.feature.user.dto.UserDto;
import com.user_service.feature.user.entity.Role;
import com.user_service.feature.user.entity.User;
import com.user_service.feature.user.enums.AccountStatus;
import com.user_service.feature.user.dto.CreateUserRequest;
import com.user_service.feature.user.dto.UpdateUserRequest;

public interface IUserService {

    UserDto getUserById(Long userId);
    UserDto createUser(CreateUserRequest userRequest);

    UserDto updateUser(Long userId, UpdateUserRequest userRequest);

    void updateUserAccountStatus(Long userId, AccountStatus accountStatus);
    Role getDefaultUserRole();


}
