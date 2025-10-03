package com.product_service.feature.cart.service.impl;

import com.product_service.feature.cart.dto.CartItemSummary;
import com.product_service.feature.cart.entity.CartItem;
import com.product_service.feature.cart.repository.CartRepository;
import com.product_service.feature.cart.request.CartItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

class CartItemServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartItemServiceImpl cartItemService;


    private CartItemSummary cartItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveCartItem() {

        //when(cartItemService.saveCartItem(any(CartItemRequest.class))).thenReturn(cartItem);
    }

    @Test
    void updateCartItem() {
    }

    @Test
    void deleteCartItem() {
    }
}