package com.user_service.feature.verificationToken.exception;

public class InvalidVerificationTokenException extends RuntimeException {

    public InvalidVerificationTokenException(String message){
        super(message);
    }
}
