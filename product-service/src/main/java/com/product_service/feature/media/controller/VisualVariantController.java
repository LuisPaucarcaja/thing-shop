package com.product_service.feature.media.controller;

import com.product_service.common.dto.ApiResponse;
import com.product_service.feature.media.service.IVisualVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/visual-variants")
public class VisualVariantController {

    private final IVisualVariantService variantService;


    @GetMapping
    public ResponseEntity<ApiResponse> getByIds(@RequestParam List<Long> ids){
        return new ResponseEntity<>(ApiResponse.success(variantService.getByIds(ids)), HttpStatus.OK);
    }
}
