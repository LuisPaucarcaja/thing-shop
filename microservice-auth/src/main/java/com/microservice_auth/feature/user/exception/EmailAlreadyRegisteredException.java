package com.microservice_auth.feature.user.exception;

public class EmailAlreadyRegisteredException extends RuntimeException{
    public EmailAlreadyRegisteredException(String message){
        super(message);
    }
}
