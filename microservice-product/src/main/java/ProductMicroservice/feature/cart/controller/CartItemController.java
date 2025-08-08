package ProductMicroservice.feature.cart.controller;

import ProductMicroservice.common.dto.ApiResponse;
import ProductMicroservice.feature.cart.request.CartItemRequest;
import ProductMicroservice.feature.cart.service.ICartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static ProductMicroservice.common.constants.GeneralConstants.ID_IN_PATH;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts/items")
public class CartItemController {


    private final ICartItemService cartItemService;

    @PostMapping
    public ResponseEntity<ApiResponse> saveCartItem(@Valid @RequestBody CartItemRequest cartItemsRequest){
        return new ResponseEntity<>(ApiResponse.success(cartItemService.saveCartItem(cartItemsRequest)), HttpStatus.OK);
    }

    @PutMapping(ID_IN_PATH)
    public ResponseEntity<ApiResponse> updateCartItem(@PathVariable Long id,
                                                      @Valid @RequestBody CartItemRequest cartItemsRequest ){
        cartItemsRequest.setId(id);
        return new ResponseEntity<>(ApiResponse.success(cartItemService.updateCartItem(cartItemsRequest)), HttpStatus.OK);
    }

    @DeleteMapping(ID_IN_PATH)
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long id){
        cartItemService.deleteCartItem(id);
        return new ResponseEntity<>(ApiResponse.success(), HttpStatus.NO_CONTENT);
    }
}
