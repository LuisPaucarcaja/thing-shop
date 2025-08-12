package ProductMicroservice.feature.cart.mapper;


import ProductMicroservice.feature.cart.dto.CartDetail;
import ProductMicroservice.feature.cart.dto.CartSummary;
import ProductMicroservice.feature.cart.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , uses = CartItemMapper.class)
public interface CartMapper {

    @Mapping(source = "cartItems", target = "cartItems")
    CartDetail toDetailDto(Cart cart);


    @Mapping(source = "cartItems", target = "cartItems")
    CartSummary toSummaryDto(Cart cart);
}
