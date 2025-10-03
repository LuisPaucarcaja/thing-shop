package com.product_service.feature.variant.controller;

import com.product_service.common.dto.ApiResponse;
import com.product_service.feature.variant.service.IVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class VariantController {


    private final IVariantService variantService;


    @GetMapping("/variants")
    public ResponseEntity<ApiResponse> getByIdIn(@RequestParam List<Long> ids){
        return new ResponseEntity<>(ApiResponse.success(variantService.getByIdIn(ids)), HttpStatus.OK);
    }

    @GetMapping("/{productId}/variants")
    public ResponseEntity<ApiResponse> getByProductId(@PathVariable Long productId){
        return new ResponseEntity<>(ApiResponse.success(variantService.getByProductId(productId)), HttpStatus.OK);
    }

}
