package com.microservice_auth.feature.verificationToken.service.impl;

import com.microservice_auth.common.exception.InvalidAccountStatusException;
import com.microservice_auth.feature.verificationToken.exception.InvalidVerificationTokenException;
import com.microservice_auth.feature.user.entity.User;
import com.microservice_auth.feature.verificationToken.entity.VerificationToken;
import com.microservice_auth.feature.user.enums.AccountStatus;
import com.microservice_auth.common.exception.RateLimitException;
import com.microservice_auth.common.exception.ResourceNotFoundException;
import com.microservice_auth.feature.verificationToken.repository.IVerificationTokenRepository;
import com.microservice_auth.feature.verificationToken.service.IVerificationTokenService;
import com.microservice_auth.feature.user.service.IUserService;
import com.microservice_auth.feature.verificationToken.service.helper.TokenEmailHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import static com.microservice_auth.common.constants.ErrorMessageConstants.*;
import static com.microservice_auth.feature.verificationToken.constants.VerificationTokenConstants.VERIFICATION_TOKEN_RESEND_INTERVAL_SECONDS;
import static com.microservice_auth.feature.verificationToken.constants.VerificationTokenMessages.*;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements IVerificationTokenService {

    private final IVerificationTokenRepository verificationTokenRepository;
    private final TokenEmailHelper tokenEmailHelper;
    private final IUserService userService;



    @Override
    @Transactional
    public void resendVerificationToken(String email) {
        User user = userService.getUserByEmail(email);
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

        tokenEmailHelper.createAndSendToken(user);
    }



    @Override
    @Transactional
    public void activateUserAccount(String token) {
        VerificationToken verificationToken = getByToken(token);

        if (isTokenInvalidOrExpired(verificationToken)) {
            throw new InvalidVerificationTokenException(TOKEN_USED_OR_EXPIRED);
        }

        userService.updateUserAccountStatus(verificationToken.getUser().getId(), AccountStatus.ACTIVE);

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

        userService.updateUserAccountStatus(verificationToken.getUser().getId(), AccountStatus.SUSPENDED);
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
