package com.microservice_auth.feature.auth.service.impl;

import com.authcore.security.AuthenticatedUserService;
import com.microservice_auth.feature.auth.constants.AuthMessages;
import com.microservice_auth.feature.auth.exception.InvalidLoginMethodException;
import com.microservice_auth.feature.user.enums.AuthProvider;
import com.microservice_auth.feature.user.enums.RoleName;
import com.microservice_auth.common.exception.UnauthorizedAccessException;
import com.microservice_auth.feature.auth.service.helper.GoogleAuthService;
import com.microservice_auth.feature.user.dto.UserDto;
import com.microservice_auth.feature.user.entity.User;
import com.microservice_auth.feature.user.enums.AccountStatus;
import com.microservice_auth.feature.auth.dto.AuthRequest;
import com.microservice_auth.feature.auth.service.IAuthService;
import com.microservice_auth.feature.user.service.IUserService;
import com.microservice_auth.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.microservice_auth.common.constants.ErrorMessageConstants.*;


@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final GoogleAuthService googleAuthService;

    private final IUserService userService;
    private final AuthenticatedUserService authenticatedUserService;

    @Override
    public String login(AuthRequest authRequest) throws AuthenticationException {
        User user = userService.getUserByEmail(authRequest.getEmail());
        handleAccountStatus(user.getAccountStatus());
        if(user.getProvider() != AuthProvider.LOCAL){
            throw new InvalidLoginMethodException(
                    String.format(AuthMessages.ACCOUNT_CREATED_WITH_PROVIDER, user.getProvider())
            );
        }
        authenticateCredentials(authRequest);
        return jwtUtil.generateToken(String.valueOf(user.getId()), RoleName.ROLE_USER);
    }


    @Override
    @Transactional
    public String loginWithGoogle(String tokenId) throws Exception {
        Long userId = googleAuthService.authenticateWithGoogle(tokenId);
        return jwtUtil.generateToken(String.valueOf(userId), RoleName.ROLE_USER);
    }


    @Override
    public UserDto validateToken() {
        return userService.getUserById(authenticatedUserService.getAuthenticatedUserId());
    }

    private void handleAccountStatus(AccountStatus accountStatus) {
        switch (accountStatus) {
            case ACTIVE -> {}
            case PENDING_VERIFICATION -> throw new UnauthorizedAccessException(ACCOUNT_PENDING_VERIFICATION);
            case SUSPENDED -> throw new UnauthorizedAccessException(ACCOUNT_SUSPENDED);
            case BANNED -> throw new UnauthorizedAccessException(ACCOUNT_BANNED);
            default -> throw new UnauthorizedAccessException(INVALID_ACCOUNT_STATUS);
        }
    }

    private void authenticateCredentials(AuthRequest authRequest) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
    }


}
