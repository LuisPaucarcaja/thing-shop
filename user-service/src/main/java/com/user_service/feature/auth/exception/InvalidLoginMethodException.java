package com.user_service.feature.auth.exception;


public class InvalidLoginMethodException extends RuntimeException {
    public InvalidLoginMethodException(String message) {
        super(message);
    }
}
