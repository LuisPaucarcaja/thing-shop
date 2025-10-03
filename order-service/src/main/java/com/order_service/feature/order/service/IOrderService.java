package com.order_service.feature.order.service;

import com.order_service.feature.order.dto.OrderDto;
import com.order_service.feature.order.dto.OrderRequest;
import com.order_service.common.dto.PaginatedResponse;

public interface IOrderService {

    OrderDto getOrderById(Long id);
    PaginatedResponse<OrderDto> getOrdersByUser(Long userId, int page);

    String createOrder(OrderRequest orderRequest);


}
