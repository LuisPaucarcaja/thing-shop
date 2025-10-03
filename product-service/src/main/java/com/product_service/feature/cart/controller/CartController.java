package com.product_service.feature.cart.controller;

import com.product_service.common.dto.ApiResponse;
import com.product_service.feature.cart.dto.CartSummary;
import com.product_service.feature.cart.service.ICartService;
import com.auth_core.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final ICartService cartService;
    private final AuthenticatedUserService authenticatedUserService;

    @GetMapping
    public ResponseEntity<ApiResponse> getCartByUser(){
        Long userId = authenticatedUserService.getAuthenticatedUserId();
        return new ResponseEntity<>(ApiResponse.success( cartService.getCartForUser(userId)),HttpStatus.OK);
    }

    @GetMapping("/checkout")
    public CartSummary getCartToCreateOrder(){
        Long userId = authenticatedUserService.getAuthenticatedUserId();
        return cartService.getCartToCreateOrder(userId);
    }

}
