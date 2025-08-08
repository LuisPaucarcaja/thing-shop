package MicroservicePago.feature.order.controller;

import MicroservicePago.feature.order.service.IOrderPaymentService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order-payments")
public class OrderPaymentController {

    private final IOrderPaymentService orderPaymentService;

    @PostMapping("/notifications")
    public ResponseEntity<Void> handlePaymentNotification(@RequestBody JsonNode payload) {
        orderPaymentService.processPaymentCreatedEvent(payload);
        return ResponseEntity.ok().build();
    }


}
