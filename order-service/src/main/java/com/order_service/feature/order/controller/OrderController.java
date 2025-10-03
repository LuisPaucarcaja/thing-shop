package com.order_service.feature.order.controller;


import com.order_service.common.exception.UnauthorizedAccessException;
import com.order_service.feature.order.dto.OrderRequest;
import com.order_service.feature.order.entity.OrderItem;
import com.order_service.common.dto.ApiResponse;
import com.order_service.feature.order.service.IOrderService;
import com.auth_core.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.order_service.common.constants.ErrorMessageConstants.UNAUTHORIZED_ACCESS;
import static com.order_service.common.constants.GeneralConstants.DEFAULT_PAGE_NUMBER;
import static com.order_service.common.constants.GeneralConstants.ID_IN_PATH;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final IOrderService orderService;
    private final AuthenticatedUserService authenticatedUserService;

    @Value("${m2m.apikey}")
    private String m2mApiKey;

    @GetMapping(ID_IN_PATH)
    public List<OrderItem> getOrderById(@PathVariable Long id,
                                        @RequestHeader(value = "X-Internal-Key", required = false) String key)  {
        if (!key.equals(m2mApiKey)) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS);
        }
        return orderService.getOrderById(id).getOrderItems();
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(@RequestBody OrderRequest orderRequest)  {
        return new ResponseEntity<>(ApiResponse.success(orderService.createOrder(orderRequest)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getOrdersByUser(@RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page){
        Long userId = authenticatedUserService.getAuthenticatedUserId();
        return new ResponseEntity<>(ApiResponse.success(orderService.getOrdersByUser(userId, page)), HttpStatus.OK);
    }


}
