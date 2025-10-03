package com.user_service.feature.verificationToken.service.impl;

import com.user_service.common.event.producer.EmailVerificationProducer;
import com.user_service.common.exception.InvalidAccountStatusException;
import com.user_service.common.util.JwtUtil;
import com.user_service.feature.user.service.IUserAccountService;
import com.user_service.feature.user.service.IUserLookupService;
import com.user_service.feature.verificationToken.exception.InvalidVerificationTokenException;
import com.user_service.feature.user.entity.User;
import com.user_service.feature.verificationToken.entity.VerificationToken;
import com.user_service.feature.user.enums.AccountStatus;
import com.user_service.common.exception.RateLimitException;
import com.user_service.common.exception.ResourceNotFoundException;
import com.user_service.feature.verificationToken.repository.IVerificationTokenRepository;
import com.user_service.feature.verificationToken.service.IVerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import static com.user_service.common.constants.ErrorMessageConstants.*;
import static com.user_service.feature.verificationToken.constants.VerificationTokenConstants.EMAIL_TOKEN_EXPIRATION_HOURS;
import static com.user_service.feature.verificationToken.constants.VerificationTokenConstants.VERIFICATION_TOKEN_RESEND_INTERVAL_SECONDS;
import static com.user_service.feature.verificationToken.constants.VerificationTokenMessages.*;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements IVerificationTokenService {

    private final IVerificationTokenRepository verificationTokenRepository;
    private final EmailVerificationProducer emailVerificationProducer;
    private final JwtUtil jwtUtil;

    private final IUserLookupService userLookupService;
    private final IUserAccountService userAccountService;


    @Override
    public VerificationToken createToken(User user) {
        String token = jwtUtil.generateEmailVerificationToken(user.getEmail());
        return verificationTokenRepository.save(
                VerificationToken.builder()
                        .token(token)
                        .user(user)
                        .expiresAt(LocalDateTime.now().plusHours(EMAIL_TOKEN_EXPIRATION_HOURS))
                        .used(false)
                        .build()
        );

    }

    @Override
    @Transactional
    public void resendVerificationToken(String email) {
        User user = userLookupService.getUserByEmail(email);
        validateAccountStatusForResend(user.getAccountStatus());

        var latestTokenOpt = verificationTokenRepository.findByUser(user);
        if (latestTokenOpt.isPresent()) {
            VerificationToken latestToken = latestTokenOpt.get();
            Duration duration = Duration.between(latestToken.getCreatedAt(), LocalDateTime.now());

            if (duration.getSeconds() < VERIFICATION_TOKEN_RESEND_INTERVAL_SECONDS) {
                long remainingSeconds = VERIFICATION_TOKEN_RESEND_INTERVAL_SECONDS - duration.getSeconds();
                throw new RateLimitException(RESEND_EMAIL_RATE_LIMIT, remainingSeconds);
            }


            verificationTokenRepository.deleteByUser(user);
        }
        VerificationToken verificationToken = createToken(user);

        emailVerificationProducer.sendVerificationTokenByEmail(verificationToken.getUser().getEmail(),
                verificationToken.getToken());
    }



    @Override
    @Transactional
    public void activateUserAccount(String token) {
        VerificationToken verificationToken = getByToken(token);

        if (isTokenInvalidOrExpired(verificationToken)) {
            throw new InvalidVerificationTokenException(TOKEN_USED_OR_EXPIRED);
        }

        userAccountService.updateUserAccountStatus(verificationToken.getUser().getId(), AccountStatus.ACTIVE);

        verificationToken.setUsed(true);
        verificationTokenRepository.save(verificationToken);

    }

    @Override
    @Transactional
    public void reportUserAccount(String token) {
        VerificationToken verificationToken = getByToken(token);

        if(isTokenInvalidOrExpired(verificationToken)){
            throw new InvalidVerificationTokenException(TOKEN_USED_OR_EXPIRED);
        }

        userAccountService.updateUserAccountStatus(verificationToken.getUser().getId(), AccountStatus.SUSPENDED);
        verificationToken.setUsed(true);
        verificationTokenRepository.save(verificationToken);

    }

    private void validateAccountStatusForResend(AccountStatus userStatus) {
        switch (userStatus) {
            case ACTIVE ->
                    throw new InvalidAccountStatusException(ACCOUNT_ALREADY_ACTIVE, HttpStatus.BAD_REQUEST);

            case SUSPENDED ->
                    throw new InvalidAccountStatusException(ACCOUNT_SUSPENDED, HttpStatus.FORBIDDEN);

            case BANNED ->
                    throw new InvalidAccountStatusException(ACCOUNT_BANNED, HttpStatus.FORBIDDEN);
        }
    }

    private VerificationToken getByToken(String token){
        return verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException(VerificationToken.class.getSimpleName(),
                        "token", token));
    }

    private boolean isTokenInvalidOrExpired(VerificationToken tokenEntity) {
        return tokenEntity.isUsed() || tokenEntity.getExpiresAt().isBefore(LocalDateTime.now());
    }


}
