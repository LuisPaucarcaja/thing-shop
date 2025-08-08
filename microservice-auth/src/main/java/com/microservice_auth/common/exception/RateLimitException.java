package com.microservice_auth.common.exception;

import lombok.Getter;

@Getter
public class RateLimitException extends RuntimeException{

    private final long secondsRemaining;

    public RateLimitException(String message, long secondsRemaining){
        super(message);
        this.secondsRemaining = secondsRemaining;
    }

}
