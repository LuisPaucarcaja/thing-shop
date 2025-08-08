package com.Inventory.feature.reservation.event.consumer;

import com.Inventory.feature.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReservationEventConsumer {

    private final ReservationService reservationService;

    @RabbitListener(queues = "inventory.reservation.created.queue")
    public void handleReservationCreated(Long orderId) {
        try {
            reservationService.createReservations(orderId);
        } catch (Exception e) {
            log.error("Unexpected error creating reservation", e);
            throw e;
        }
    }


    @RabbitListener(queues = "inventory.reservation.confirmed.queue")
    public void handleReservationConfirmed(Long orderId) {
        try {

            reservationService.confirmReservations(orderId);
        } catch (Exception e){
            log.error("Error confirming reservation", e);
            throw e;
        }
    }

}
