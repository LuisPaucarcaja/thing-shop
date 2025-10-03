package com.notification_service.event.consumer;

import com.notification_service.event.dto.EmailVerificationEvent;
import com.notification_service.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.notification_service.config.RabbitMQConfig.QUEUE;

@RequiredArgsConstructor
@Component
public class EmailVerificationConsumer {

    private final INotificationService notificationService;

    @RabbitListener(queues = QUEUE)
    public void handleEmailVerification(EmailVerificationEvent event) {
        notificationService.sendRegistrationVerificationEmail(event.getEmail(), event.getToken());
    }
}
