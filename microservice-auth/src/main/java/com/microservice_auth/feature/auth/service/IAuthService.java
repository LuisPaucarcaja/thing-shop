package com.microservice_auth.feature.auth.service;

import com.microservice_auth.feature.user.dto.UserDto;
import com.microservice_auth.feature.auth.dto.AuthRequest;

public interface IAuthService {

    String login(AuthRequest authRequest);

    String loginWithGoogle(String tokenId) throws Exception;

    UserDto validateToken();

}
