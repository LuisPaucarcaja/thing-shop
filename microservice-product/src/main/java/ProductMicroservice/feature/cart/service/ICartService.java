package ProductMicroservice.feature.cart.service;

import ProductMicroservice.feature.cart.dto.CartDetail;
import ProductMicroservice.feature.cart.dto.CartSummary;
import ProductMicroservice.feature.cart.entity.Cart;


public interface ICartService {

    CartSummary getCartToCreateOrder(Long userId);

    CartDetail getCartForUser(Long userId);

    Cart getOrCreateCartByUser(Long userId);

    void invalidateCartCache(Long userId);

}
