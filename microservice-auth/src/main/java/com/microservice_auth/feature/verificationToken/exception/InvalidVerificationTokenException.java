package com.microservice_auth.feature.verificationToken.exception;

public class InvalidVerificationTokenException extends RuntimeException {

    public InvalidVerificationTokenException(String message){
        super(message);
    }
}
