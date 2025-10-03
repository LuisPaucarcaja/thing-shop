package com.product_service.feature.product.service.impl;

import com.product_service.feature.product.dto.BrandDto;
import com.product_service.feature.product.dto.CategoryDto;
import com.product_service.feature.product.mapper.BrandMapper;
import com.product_service.feature.product.mapper.CategoryMapper;
import com.product_service.feature.product.repository.CategoryRepository;
import com.product_service.feature.product.repository.ProductRepository;
import com.product_service.feature.product.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.product_service.common.constants.CacheConstants.GET_ALL_CATEGORIES;
import static com.product_service.common.constants.CacheConstants.GET_BRANDS_BY_CATEGORY;

@RequiredArgsConstructor
@Service
public class CategoryServiceImp implements ICategoryService {


    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final BrandMapper brandMapper;
    private final CategoryMapper categoryMapper;

    @Override
    @Cacheable(GET_ALL_CATEGORIES)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    @Cacheable(GET_BRANDS_BY_CATEGORY)
    public List<BrandDto> getBrandsByCategory(Long categoryId) {
        return productRepository.findBrandsByCategoryId(categoryId).stream()
                .map(brandMapper::toBrandDto).collect(Collectors.toList());
    }

}
