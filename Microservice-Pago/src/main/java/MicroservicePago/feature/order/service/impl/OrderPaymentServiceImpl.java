package MicroservicePago.feature.order.service.impl;

import MicroservicePago.common.exception.PaymentProcessingException;
import MicroservicePago.common.exception.ResourceNotFoundException;
import MicroservicePago.feature.order.entity.Order;
import MicroservicePago.feature.order.entity.OrderPayment;
import MicroservicePago.feature.order.enums.OrderStatus;
import MicroservicePago.feature.order.event.producer.ReservationEventProducer;
import MicroservicePago.feature.order.repository.OrderPaymentRepository;
import MicroservicePago.feature.order.repository.OrderRepository;
import MicroservicePago.feature.order.service.IOrderPaymentService;
import MicroservicePago.feature.order.service.cache.OrderCacheService;
import MicroservicePago.feature.order.service.helper.MercadoPagoService;
import com.fasterxml.jackson.databind.JsonNode;
import com.mercadopago.resources.payment.Payment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import static MicroservicePago.common.constants.ErrorMessageConstants.*;
import static MicroservicePago.common.constants.GeneralConstants.FIELD_ID;
import static MicroservicePago.common.constants.MercadoPagoConstants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderPaymentServiceImpl implements IOrderPaymentService {

    private final OrderPaymentRepository orderPaymentRepository;
    private final OrderRepository orderRepository;
    private final ReservationEventProducer reservationEventProducer;
    private final OrderCacheService orderCacheService;
    private final MercadoPagoService mercadoPagoService;

    @Override
    public void processPaymentCreatedEvent(JsonNode payload) {
        String action = payload.path(ACTION_FIELD).asText(null);

        if (!EventType.PAYMENT_CREATED.getValue().equals(action)) {
            log.debug("Ignoring non-payment.created event: {}", action);
            return;
        }

        String paymentId = extractPaymentId(payload);

        try {
            Payment payment = mercadoPagoService.getPaymentDetails(paymentId);
            Long orderId = extractOrderIdFromMetadata(payment);

            if (PaymentStatus.APPROVED.getValue().equals(payment.getStatus())) {
                log.info("Approved payment received. PaymentID: {}, OrderID: {}", paymentId, orderId);
                OrderPayment orderPayment = buildOrderPayment(payment);
                confirmOrderPayment(orderId, orderPayment);
            } else {
                log.info("Received non-approved payment. Status: {}, PaymentID: {}", payment.getStatus(), paymentId);
            }

        } catch (NumberFormatException e) {
            log.error("Invalid order ID format", e);
            throw new PaymentProcessingException("Invalid order ID format in metadata");
        } catch (Exception e) {
            log.error("Failed to process payment: {}", e.getMessage(), e);
            throw new PaymentProcessingException(PAYMENT_PROCESS_FAIL + e.getMessage());
        }
    }

    private String extractPaymentId(JsonNode payload) {
        String paymentId = payload.path(DATA_FIELD).path("id").asText(null);
        if (paymentId == null) {
            throw new PaymentProcessingException(PAYMENT_ID_MISSING);
        }
        return paymentId;
    }

    private Long extractOrderIdFromMetadata(Payment payment) {
        if (payment.getMetadata() == null || !payment.getMetadata().containsKey(ORDER_ID_FIELD)) {
            throw new PaymentProcessingException(ORDER_ID_MISSING);
        }

        Object orderIdObj = payment.getMetadata().get(ORDER_ID_FIELD);

        if (orderIdObj instanceof Number) {
            return ((Number) orderIdObj).longValue();
        } else if (orderIdObj instanceof String) {
            return Long.parseLong((String) orderIdObj);
        }

        throw new PaymentProcessingException("Unsupported orderId type: " + orderIdObj.getClass().getSimpleName());
    }

    private OrderPayment buildOrderPayment(Payment payment) {
        return OrderPayment.builder()
                .paymentId(payment.getId().toString())
                .paymentMethod(payment.getPaymentMethodId())
                .amountPaid(payment.getTransactionDetails().getTotalPaidAmount())
                .paymentDate(payment.getDateApproved().toLocalDateTime())
                .build();
    }

    @Transactional
    public void confirmOrderPayment(Long orderId, OrderPayment payment) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(Order.class.getSimpleName(), FIELD_ID, orderId));

        if (isPending(order)) {
            order.setStatus(OrderStatus.PAID);
            payment.setOrder(order);
            orderPaymentRepository.save(payment);

            reservationEventProducer.sendReservationConfirmedEvent(orderId);
            orderCacheService.evictOrdersByUser(order.getUserId());

            log.info("Order confirmed and reservation event sent. OrderID: {}", orderId);
        } else {
            log.warn("Order is not in PENDING_PAYMENT state. OrderID: {}, CurrentStatus: {}", orderId, order.getStatus());
        }
    }

    private boolean isPending(Order order) {
        return order.getStatus() == OrderStatus.PENDING_PAYMENT;
    }
}
