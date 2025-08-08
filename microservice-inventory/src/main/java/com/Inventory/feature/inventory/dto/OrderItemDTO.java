package com.Inventory.feature.inventory.dto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {

    private Long id;

    private Long productVariantId;
    private BigDecimal unitPrice;
    private int quantity;
}
