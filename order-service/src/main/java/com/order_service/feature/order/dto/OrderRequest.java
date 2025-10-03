package com.order_service.feature.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderRequest {

    @NotNull
    private Long shippingAddressId;

}
