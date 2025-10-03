package com.auth_core.exception;

public class TokenValidationException extends RuntimeException{

    public TokenValidationException(String message){
        super(message);
    }
}
