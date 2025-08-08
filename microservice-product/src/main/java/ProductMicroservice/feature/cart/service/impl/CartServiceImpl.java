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
import org.springframework.transaction.annotation.Transactional;

import static ProductMicroservice.common.constants.CacheConstants.GET_CART_BY_USER;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements ICartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;


    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = GET_CART_BY_USER, key = "#userId", unless = "#result == null")
    public CartDetail getCartForUser(Long userId) {
        return cartMapper.toDTOUser(cartRepository.findByUserId(userId));
    }

    @Override
    public CartSummary getCartToCreateOrder(Long userId) {
        return cartMapper.toDto(cartRepository.findByUserId(userId));
    }


    @Override
    public Cart getOrCreateCartByUser(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if(cart == null){
            return cartRepository.save(Cart.builder().userId(userId).build());
        }
        return cart;
    }

    @CacheEvict(value = GET_CART_BY_USER, key = "#userId")
    @Override
    public void invalidateCartCache(Long userId) {
    }
}
