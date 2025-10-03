package com.product_service.feature.cart.dto;

import com.product_service.feature.variant.dto.VariantDetailDTO;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CartItemDetail {


    private Long id;

    private VariantDetailDTO productVariant;
    private int quantity;

}
