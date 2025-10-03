package com.order_service.feature.shipping.controller;


import com.order_service.feature.shipping.dto.ShippingAddressDTO;
import com.order_service.common.dto.ApiResponse;
import com.order_service.feature.shipping.request.ShippingAddressRequest;
import com.order_service.feature.shipping.service.IShippingAddressService;
import com.auth_core.security.AuthenticatedUserService;
import com.order_service.common.constants.GeneralConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/shipments/addresses")
public class ShippingAddressController {


    private final IShippingAddressService shippingAddressService;
    private final AuthenticatedUserService authenticatedUserService;

    @GetMapping
    public ResponseEntity<ApiResponse> getByUserId(){
        Long userId = authenticatedUserService.getAuthenticatedUserId();
        return new ResponseEntity<>(ApiResponse.success(shippingAddressService.getByUserId(userId)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> saveShippingAddress(@RequestBody ShippingAddressRequest addressRequest){
        Long userId = authenticatedUserService.getAuthenticatedUserId();
        ShippingAddressDTO shippingAddress = shippingAddressService.saveShippingAddress(userId, addressRequest);
        return new ResponseEntity<>(ApiResponse.success(shippingAddress), HttpStatus.CREATED);
    }

    @PutMapping(GeneralConstants.ID_IN_PATH)
    public ResponseEntity<ApiResponse> updateShippingAddress(@PathVariable Long id,
                                                             @Valid @RequestBody ShippingAddressRequest addressRequest){
        Long userId = authenticatedUserService.getAuthenticatedUserId();
        ShippingAddressDTO shippingAddress = shippingAddressService.updateShippingAddress(id, userId, addressRequest);
        return new ResponseEntity<>(ApiResponse.success(shippingAddress), HttpStatus.OK);
    }


}
