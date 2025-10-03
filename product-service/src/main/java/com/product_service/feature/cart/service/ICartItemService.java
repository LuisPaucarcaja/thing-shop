package com.product_service.feature.cart.service;

import com.product_service.feature.cart.dto.CartItemSummary;
import com.product_service.feature.cart.request.CartItemRequest;


public interface ICartItemService {


    CartItemSummary saveCartItem(CartItemRequest cartItemRequest);

    CartItemSummary updateCartItem(CartItemRequest cartItemRequest);

    void deleteCartItem(Long cartItemId);
}
