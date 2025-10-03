package com.product_service.feature.cart.service.impl;

import com.product_service.feature.cart.dto.CartItemSummary;
import com.product_service.common.exception.ResourceNotFoundException;
import com.product_service.common.exception.UnauthorizedAccessException;
import com.product_service.feature.cart.mapper.CartItemMapper;
import com.product_service.feature.cart.entity.Cart;
import com.product_service.feature.cart.entity.CartItem;
import com.product_service.feature.cart.repository.CartItemRepository;
import com.product_service.feature.cart.request.CartItemRequest;
import com.product_service.feature.cart.service.ICartItemService;
import com.product_service.feature.cart.service.ICartService;
import com.product_service.feature.variant.service.IVariantService;
import com.auth_core.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.product_service.common.constants.ErrorMessageConstants.UNAUTHORIZED_ACCESS;
import static com.product_service.common.constants.GeneralConstants.FIELD_ID;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final IVariantService variantService;
    private final ICartService cartService;
    private final AuthenticatedUserService authenticatedUserService;

    @Override
    @Transactional
    public CartItemSummary saveCartItem(CartItemRequest cartItemRequest) {
        Long userId = authenticatedUserService.getAuthenticatedUserId();
        Cart cart = cartService.getOrCreateCartByUser(userId);

        variantService.getById(cartItemRequest.getVariantId());

        CartItem existingCartItem = cartItemRepository.findByCartIdAndProductVariantId(cart.getId(),
                cartItemRequest.getVariantId());

        CartItem savedItem;

        if (existingCartItem != null) {
            existingCartItem.setQuantity(cartItemRequest.getQuantity());
            savedItem = cartItemRepository.save(existingCartItem);
        } else {
            var newCartItem = cartItemMapper.toEntity(cartItemRequest);
            newCartItem.setCart(cart);
            savedItem = cartItemRepository.save(newCartItem);
        }

        cartService.invalidateCartCache(userId);
        return cartItemMapper.toSummary(savedItem);
    }


    @Override
    @Transactional
    public CartItemSummary updateCartItem(CartItemRequest cartItemRequest) {
        Long userId = authenticatedUserService.getAuthenticatedUserId();
        CartItem cartItem = getById(cartItemRequest.getId());

        validateCartItemOwnership(cartItem, userId);
        cartItem.setQuantity(cartItemRequest.getQuantity());

        var updatedItem = cartItemRepository.save(cartItem);
        cartService.invalidateCartCache(userId);
        return cartItemMapper.toSummary(updatedItem);
    }

    @Transactional
    @Override
    public void deleteCartItem(Long cartItemId) {
        Long userId = authenticatedUserService.getAuthenticatedUserId();
        CartItem cartItem = getById(cartItemId);
        validateCartItemOwnership(cartItem, userId);
        cartItemRepository.delete(cartItem);
        cartService.invalidateCartCache(userId);
    }

    private CartItem getById(Long id){
        return cartItemRepository.findById(id).orElseThrow( () ->
                new ResourceNotFoundException(CartItem.class.getSimpleName(), FIELD_ID , id));
    }

    private void validateCartItemOwnership(CartItem cartItem, Long userId) {
        if (!cartItem.getCart().getUserId().equals(userId)) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS);
        }
    }



}
