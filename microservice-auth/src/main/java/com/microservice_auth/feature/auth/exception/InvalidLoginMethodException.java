package com.microservice_auth.feature.auth.exception;


public class InvalidLoginMethodException extends RuntimeException {
    public InvalidLoginMethodException(String message) {
        super(message);
    }
}
