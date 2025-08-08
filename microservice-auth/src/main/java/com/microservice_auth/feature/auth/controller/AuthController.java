package com.microservice_auth.feature.auth.controller;


import com.microservice_auth.common.exception.UnauthorizedAccessException;
import com.microservice_auth.feature.auth.dto.AuthRequest;
import com.microservice_auth.feature.auth.dto.GoogleLoginRequest;
import com.microservice_auth.feature.auth.service.IAuthService;
import com.microservice_auth.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IAuthService authService;

    @PostMapping
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody AuthRequest authRequest){
        try {
            return new ResponseEntity<>(ApiResponse.success(authService.login(authRequest)), HttpStatus.OK);
        } catch (AuthenticationException exception){
            return new ResponseEntity<>(ApiResponse.error(exception.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/google")
    public ResponseEntity<ApiResponse> loginWithGoogle(@RequestBody GoogleLoginRequest googleLoginRequest) {
        try {
            String token = authService.loginWithGoogle(googleLoginRequest.getTokenId());
            return new ResponseEntity<>(ApiResponse.success(token), HttpStatus.OK);
        }
        catch (UnauthorizedAccessException exception) {
            return new ResponseEntity<>(ApiResponse.error(exception.getMessage()), HttpStatus.FORBIDDEN);
        }
        catch (Exception e){
            return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/validate-token")
    public ResponseEntity<ApiResponse> validateToken(){
       return new ResponseEntity<>(ApiResponse.success(authService.validateToken()), HttpStatus.OK);
    }


}
