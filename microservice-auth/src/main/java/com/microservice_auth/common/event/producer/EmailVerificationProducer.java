package com.microservice_auth.common.event.producer;

import com.microservice_auth.common.event.dto.EmailVerificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.microservice_auth.config.RabbitMQConfig.*;
@RequiredArgsConstructor
@Service
public class EmailVerificationProducer {


    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public void sendVerificationTokenByEmail(String email, String token) {
        EmailVerificationEvent event = new EmailVerificationEvent(email, token);
        rabbitTemplate.convertAndSend(USER_EXCHANGE, USER_CREATED_ROUTING_KEY, event);
    }
}
