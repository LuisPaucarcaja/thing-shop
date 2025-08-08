package ProductMicroservice.feature.cart.service;

import ProductMicroservice.feature.cart.dto.CartItemSummary;
import ProductMicroservice.feature.cart.request.CartItemRequest;


public interface ICartItemService {


    CartItemSummary saveCartItem(CartItemRequest cartItemRequest);

    CartItemSummary updateCartItem(CartItemRequest cartItemRequest);

    void deleteCartItem(Long cartItemId);
}
