package com.user_service.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidAccountStatusException extends RuntimeException {
    private final HttpStatus status;

    public InvalidAccountStatusException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
