package com.product_service.feature.cart.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartSummary {

    private Long id;

    private List<CartItemSummary> cartItems;

}
