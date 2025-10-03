package com.product_service.feature.product.service;

import com.product_service.feature.product.dto.BrandDto;
import com.product_service.feature.product.dto.CategoryDto;

import java.util.List;

public interface ICategoryService {
    List<CategoryDto> getAllCategories();
    List<BrandDto> getBrandsByCategory(Long categoryId);
}
