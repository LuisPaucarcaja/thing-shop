package MicroservicePago.feature.shipping.controller;


import MicroservicePago.feature.shipping.dto.ShippingAddressDTO;
import MicroservicePago.common.dto.ApiResponse;
import MicroservicePago.feature.shipping.request.ShippingAddressRequest;
import MicroservicePago.feature.shipping.service.IShippingAddressService;
import com.authcore.security.AuthenticatedUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static MicroservicePago.common.constants.GeneralConstants.ID_IN_PATH;

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

    @PutMapping(ID_IN_PATH)
    public ResponseEntity<ApiResponse> updateShippingAddress(@PathVariable Long id,
                                                             @Valid @RequestBody ShippingAddressRequest addressRequest){
        Long userId = authenticatedUserService.getAuthenticatedUserId();
        ShippingAddressDTO shippingAddress = shippingAddressService.updateShippingAddress(id, userId, addressRequest);
        return new ResponseEntity<>(ApiResponse.success(shippingAddress), HttpStatus.OK);
    }


}
