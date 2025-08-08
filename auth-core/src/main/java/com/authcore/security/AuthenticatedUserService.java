package com.authcore.security;

import com.authcore.dto.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserService {

    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("The user is not authenticated");
        }

        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return user.getUserId();
    }
}
