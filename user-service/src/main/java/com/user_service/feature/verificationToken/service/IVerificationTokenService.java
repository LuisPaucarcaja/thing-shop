package com.user_service.feature.verificationToken.service;


import com.user_service.feature.user.entity.User;
import com.user_service.feature.verificationToken.entity.VerificationToken;

public interface IVerificationTokenService {

    VerificationToken createToken(User user);

    void resendVerificationToken(String email);

    void activateUserAccount(String token);
    void reportUserAccount(String token);
}
