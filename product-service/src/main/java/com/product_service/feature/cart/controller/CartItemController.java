package com.product_service.feature.cart.controller;

import com.product_service.common.dto.ApiResponse;
import com.product_service.feature.cart.request.CartItemRequest;
import com.product_service.feature.cart.service.ICartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.product_service.common.constants.GeneralConstants.ID_IN_PATH;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts/items")
public class CartItemController {


    private final ICartItemService cartItemService;

    @PostMapping
    public ResponseEntity<ApiResponse> saveCartItem(@Valid @RequestBody CartItemRequest cartItemsRequest){
        return new ResponseEntity<>(ApiResponse.success(cartItemService.saveCartItem(cartItemsRequest)), HttpStatus.OK);
    }

    @PutMapping(ID_IN_PATH)
    public ResponseEntity<ApiResponse> updateCartItem(@PathVariable Long id,
                                                      @Valid @RequestBody CartItemRequest cartItemsRequest ){
        cartItemsRequest.setId(id);
        return new ResponseEntity<>(ApiResponse.success(cartItemService.updateCartItem(cartItemsRequest)), HttpStatus.OK);
    }

    @DeleteMapping(ID_IN_PATH)
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long id){
        cartItemService.deleteCartItem(id);
        return new ResponseEntity<>(ApiResponse.success(), HttpStatus.NO_CONTENT);
    }
}
