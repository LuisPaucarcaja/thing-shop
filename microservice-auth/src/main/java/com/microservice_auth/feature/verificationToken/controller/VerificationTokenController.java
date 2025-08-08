package com.microservice_auth.feature.verificationToken.controller;


import com.microservice_auth.feature.verificationToken.service.IVerificationTokenService;
import com.microservice_auth.common.dto.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/verification-tokens")
public class VerificationTokenController {

    private final IVerificationTokenService verificationTokenService;

    @GetMapping
    public ResponseEntity<ApiResponse> activateUserAccount(@RequestParam("token") String token){
         verificationTokenService.activateUserAccount(token);
        return new ResponseEntity<>(ApiResponse.success(), HttpStatus.OK);
    }

    @PostMapping("/resend")
    public ResponseEntity<ApiResponse> resendVerificationToken(@Valid @Email @RequestParam String email) {
        verificationTokenService.resendVerificationToken(email);
        return new ResponseEntity<>(ApiResponse.success(), HttpStatus.OK);
    }

    @PatchMapping("/report")
    public ResponseEntity<ApiResponse> reportUserAccount(@RequestParam("token") String token) {
        verificationTokenService.reportUserAccount(token);
        return new ResponseEntity<>(ApiResponse.success(), HttpStatus.OK);
    }

}
