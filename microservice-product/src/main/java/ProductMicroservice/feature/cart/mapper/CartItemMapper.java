package ProductMicroservice.feature.cart.mapper;

import ProductMicroservice.feature.cart.dto.CartItemDetail;
import ProductMicroservice.feature.cart.dto.CartItemSummary;
import ProductMicroservice.feature.cart.entity.CartItem;
import ProductMicroservice.feature.variant.mapper.VariantMapper;
import ProductMicroservice.feature.cart.request.CartItemRequest;
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
