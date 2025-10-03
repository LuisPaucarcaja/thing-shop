package com.order_service.feature.order.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface IOrderPaymentService {
    void processPaymentCreatedEvent(JsonNode payload);

}
