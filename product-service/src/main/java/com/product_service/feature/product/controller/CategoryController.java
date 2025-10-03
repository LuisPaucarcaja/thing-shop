package com.product_service.feature.product.controller;

import com.product_service.common.dto.ApiResponse;
import com.product_service.feature.product.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {


    private final ICategoryService categoryService;


    @GetMapping
    public ResponseEntity<ApiResponse> getAllCategories(){
        return new ResponseEntity<>(ApiResponse.success(categoryService.getAllCategories()), HttpStatus.OK);
    }

    @GetMapping("/{id}/brands")
    public ResponseEntity<ApiResponse> getBrandsByCategory(@PathVariable Long id){
        return new ResponseEntity<>(ApiResponse.success(categoryService.getBrandsByCategory(id)), HttpStatus.OK);
    }

}
