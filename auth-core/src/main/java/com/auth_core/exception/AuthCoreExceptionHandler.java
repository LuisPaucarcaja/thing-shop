package com.auth_core.exception;


import com.auth_core.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.auth_core.constants.ErrorMessageConstants.*;

@ControllerAdvice
public class AuthCoreExceptionHandler {


    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<ApiResponse> handleJwtException(TokenValidationException ex) {
        String message = ex.getMessage();
        HttpStatus status = switch (message) {
            case TOKEN_EXPIRED -> HttpStatus.UNAUTHORIZED;
            case MALFORMED_TOKEN, INVALID_TOKEN_SIGNATURE, INVALID_TOKEN -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.BAD_REQUEST;
        };


        return new ResponseEntity<>(ApiResponse.error(message), status);
    }  
}