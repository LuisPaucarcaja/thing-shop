package com.product_service.feature.cart.mapper;

import com.product_service.feature.cart.dto.CartItemDetail;
import com.product_service.feature.cart.dto.CartItemSummary;
import com.product_service.feature.cart.entity.CartItem;
import com.product_service.feature.variant.mapper.VariantMapper;
import com.product_service.feature.cart.request.CartItemRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = VariantMapper.class)
public interface CartItemMapper {



    @Mapping(target = "variantId", source = "productVariant.id")
    @Mapping(target = "price", source = "productVariant.price")
    CartItemSummary toSummary (CartItem cartItem);

    CartItemDetail toDetail (CartItem cartItem);



    @Mapping(target = "productVariant.id", source = "variantId")
    CartItem toEntity(CartItemRequest cartItemRequest);

}
