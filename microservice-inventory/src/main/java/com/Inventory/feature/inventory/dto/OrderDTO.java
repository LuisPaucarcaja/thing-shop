package com.Inventory.feature.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Long id;

    private String contactPhone;

    //private List<OrderItem> orderItems;

    private BigDecimal totalAmount;


    private LocalDateTime createdAt;
}

