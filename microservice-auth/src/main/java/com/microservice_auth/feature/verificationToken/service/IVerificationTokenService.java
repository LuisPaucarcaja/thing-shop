package com.microservice_auth.feature.verificationToken.service;


public interface IVerificationTokenService {

    void resendVerificationToken(String email);

    void activateUserAccount(String token);
    void reportUserAccount(String token);
}
