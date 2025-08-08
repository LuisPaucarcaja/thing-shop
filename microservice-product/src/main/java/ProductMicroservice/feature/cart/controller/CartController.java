package ProductMicroservice.feature.cart.controller;

import ProductMicroservice.common.dto.ApiResponse;
import ProductMicroservice.feature.cart.dto.CartSummary;
import ProductMicroservice.feature.cart.service.ICartService;
import com.authcore.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final ICartService cartService;
    private final AuthenticatedUserService authenticatedUserService;

    @GetMapping
    public ResponseEntity<ApiResponse> getCartByUser(){
        Long userId = authenticatedUserService.getAuthenticatedUserId();
        return new ResponseEntity<>(ApiResponse.success( cartService.getCartForUser(userId)),HttpStatus.OK);
    }

    @GetMapping("/checkout")
    public CartSummary getCartToCreateOrder(){
        Long userId = authenticatedUserService.getAuthenticatedUserId();
        return cartService.getCartToCreateOrder(userId);
    }

}
