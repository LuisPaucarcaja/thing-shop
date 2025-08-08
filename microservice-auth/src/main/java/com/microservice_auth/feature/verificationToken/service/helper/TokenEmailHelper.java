package com.microservice_auth.feature.verificationToken.service.helper;

import com.microservice_auth.common.util.JwtUtil;
import com.microservice_auth.common.event.producer.EmailVerificationProducer;
import com.microservice_auth.feature.user.entity.User;
import com.microservice_auth.feature.verificationToken.entity.VerificationToken;
import com.microservice_auth.feature.verificationToken.repository.IVerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.microservice_auth.feature.verificationToken.constants.VerificationTokenConstants.EMAIL_TOKEN_EXPIRATION_HOURS;

@RequiredArgsConstructor
@Service
public class TokenEmailHelper {

    private final JwtUtil jwtUtil;
    private final IVerificationTokenRepository tokenRepository;
    private final EmailVerificationProducer emailProducer;

    public void createAndSendToken(User user) {
        String jwtToken = jwtUtil.generateEmailVerificationToken(user.getEmail());
        tokenRepository.save(
                VerificationToken.builder()
                        .token(jwtToken)
                        .user(user)
                        .expiresAt(LocalDateTime.now().plusHours(EMAIL_TOKEN_EXPIRATION_HOURS))
                        .used(false)
                        .build()
        );
        emailProducer.sendVerificationTokenByEmail(user.getEmail(), jwtToken);
    }
}
