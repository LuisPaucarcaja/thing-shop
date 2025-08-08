package MicroservicePago.feature.order.event.producer;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static MicroservicePago.config.RabbitMQConfig.RESERVATION_EXCHANGE;

@RequiredArgsConstructor
@Service
public class ReservationEventProducer {


    private static final String ROUTING_KEY_CREATED = "reservation.created";
    private static final String ROUTING_KEY_CONFIRMED = "reservation.confirmed";

    private final RabbitTemplate rabbitTemplate;

    public void sendReservationCreatedEvent(Long orderId) {
        sendEvent(ROUTING_KEY_CREATED, orderId);
    }

    public void sendReservationConfirmedEvent(Long orderId) {
        sendEvent(ROUTING_KEY_CONFIRMED, orderId);
    }


    private void sendEvent(String routingKey, Long orderId) {
        rabbitTemplate.convertAndSend(RESERVATION_EXCHANGE, routingKey, orderId);
    }
}
