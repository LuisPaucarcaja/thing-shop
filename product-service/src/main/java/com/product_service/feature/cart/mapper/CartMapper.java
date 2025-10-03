package com.product_service.feature.cart.mapper;


import com.product_service.feature.cart.dto.CartDetail;
import com.product_service.feature.cart.dto.CartSummary;
import com.product_service.feature.cart.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , uses = CartItemMapper.class)
public interface CartMapper {

    @Mapping(source = "cartItems", target = "cartItems")
    CartDetail toDetailDto(Cart cart);


    @Mapping(source = "cartItems", target = "cartItems")
    CartSummary toSummaryDto(Cart cart);
}
