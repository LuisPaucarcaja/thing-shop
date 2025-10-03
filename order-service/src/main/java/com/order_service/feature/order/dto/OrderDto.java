package com.order_service.feature.order.dto;

import com.order_service.feature.order.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long id;

    private List<OrderItem> orderItems;

    private BigDecimal totalAmount;

    private LocalDateTime createdAt;

    private String shippingAddress;

    private String paymentMethod;

}
