package com.user_service.feature.auth.service;

import com.user_service.feature.user.dto.UserDto;
import com.user_service.feature.auth.dto.AuthRequest;

public interface IAuthService {

    String login(AuthRequest authRequest);

    String loginWithGoogle(String tokenId) throws Exception;

    UserDto validateToken();

}
