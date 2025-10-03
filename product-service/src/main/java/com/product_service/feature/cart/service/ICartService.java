package com.product_service.feature.cart.service;

import com.product_service.feature.cart.dto.CartDetail;
import com.product_service.feature.cart.dto.CartSummary;
import com.product_service.feature.cart.entity.Cart;


public interface ICartService {

    CartSummary getCartToCreateOrder(Long userId);

    CartDetail getCartForUser(Long userId);

    Cart getOrCreateCartByUser(Long userId);

    void invalidateCartCache(Long userId);

}
