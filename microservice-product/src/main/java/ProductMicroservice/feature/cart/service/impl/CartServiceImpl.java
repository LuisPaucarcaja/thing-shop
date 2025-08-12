package ProductMicroservice.feature.cart.service.impl;

import ProductMicroservice.feature.cart.dto.CartDetail;
import ProductMicroservice.feature.cart.dto.CartSummary;
import ProductMicroservice.feature.cart.mapper.CartMapper;
import ProductMicroservice.feature.cart.entity.Cart;
import ProductMicroservice.feature.cart.repository.CartRepository;
import ProductMicroservice.feature.cart.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static ProductMicroservice.common.constants.CacheConstants.GET_CART_BY_USER;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements ICartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Override
    @Cacheable(value = GET_CART_BY_USER, key = "#userId", unless = "#result == null")
    public CartDetail getCartForUser(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        return cartMapper.toDetailDto(cart);
    }

    @Override
    public CartSummary getCartToCreateOrder(Long userId) {
        return cartMapper.toSummaryDto(cartRepository.findByUserId(userId));
    }


    @Override
    public Cart getOrCreateCartByUser(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if(cart == null){
            return cartRepository.save(Cart.builder().userId(userId).build());
        }
        return cart;
    }

    @Override
    @CacheEvict(value = GET_CART_BY_USER, key = "#userId")
    public void invalidateCartCache(Long userId) {
    }
}
