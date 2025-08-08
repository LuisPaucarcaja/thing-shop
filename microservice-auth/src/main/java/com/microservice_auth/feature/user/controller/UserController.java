package com.microservice_auth.feature.user.controller;


import com.authcore.security.AuthenticatedUserService;
import com.microservice_auth.feature.user.dto.UserDto;
import com.microservice_auth.feature.user.dto.CreateUserRequest;
import com.microservice_auth.feature.user.dto.UpdateUserRequest;
import com.microservice_auth.feature.user.service.IUserService;
import com.microservice_auth.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    public final IUserService userService;
    private final AuthenticatedUserService authenticatedUserService;

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@Valid @ModelAttribute CreateUserRequest userRequest){
        UserDto userSaved = userService.createUser(userRequest);
        return new ResponseEntity<>(ApiResponse.success(userSaved), HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<ApiResponse> updateUser( @Valid @ModelAttribute UpdateUserRequest userRequest ) {
        Long userId = authenticatedUserService.getAuthenticatedUserId();
        UserDto userSaved = userService.updateUser(userId, userRequest);
        return new ResponseEntity<>(ApiResponse.success(userSaved), HttpStatus.OK);

    }

}
