package com.order_service.feature.shipping.controller;

import com.order_service.common.dto.ApiResponse;
import com.order_service.feature.shipping.service.IShippingCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/shipments/cities")
public class ShippingCityController {

    private final IShippingCityService shippingCityService;


    @GetMapping
    public ResponseEntity<ApiResponse> getActiveCitiesAndDistricts(){
        return new ResponseEntity<>(ApiResponse.success(shippingCityService.getActiveCitiesAndDistricts()), HttpStatus.OK);
    }
}
