package com.notification_microservice.service.impl;

import com.notification_microservice.exception.EmailDeliveryException;
import com.notification_microservice.service.email.builder.EmailBuildService;
import com.notification_microservice.service.email.sender.EmailSenderService;
import com.notification_microservice.service.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements INotificationService {

    private final EmailSenderService emailSenderService;

    private final EmailBuildService emailBuildService;


    @Override
    public void sendRegistrationVerificationEmail(String email, String token) {
        try {
            var body = emailBuildService.buildVerificationEmailBody(email, token);
            emailSenderService.sendEmail(email , "Thing Shop - Email Verification",
                    emailBuildService.wrapWithTemplate(body));
        } catch (Exception e) {
            log.error("Failed to send verification email to {}: {}", email, e.getMessage(), e);
            throw new EmailDeliveryException("Failed to send email to " + email, e);
        }
    }



}
