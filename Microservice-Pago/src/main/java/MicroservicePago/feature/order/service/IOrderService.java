package MicroservicePago.feature.order.service;

import MicroservicePago.feature.order.dto.OrderDto;
import MicroservicePago.feature.order.dto.OrderRequest;
import MicroservicePago.common.dto.PaginatedResponse;

public interface IOrderService {

    OrderDto getOrderById(Long id);
    PaginatedResponse<OrderDto> getOrdersByUser(Long userId, int page);

    String createOrder(OrderRequest orderRequest);


}
