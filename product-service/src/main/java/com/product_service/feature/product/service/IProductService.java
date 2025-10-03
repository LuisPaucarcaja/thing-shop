package com.product_service.feature.product.service;

import com.product_service.common.dto.PaginatedResponse;
import com.product_service.feature.product.dto.PriceRangeDto;
import com.product_service.feature.product.dto.ProductDetailDto;
import com.product_service.feature.product.dto.ProductPreviewDto;
import com.product_service.feature.product.dto.ProductSummary;

import java.math.BigDecimal;
import java.util.List;

public interface IProductService {

    PaginatedResponse<ProductPreviewDto> getByCategoryAndBrandAndPriceRange(Long categoryId, Long brandId,
                                                                            BigDecimal minPrice, BigDecimal maxPrice,
                                                                            int page);

    PriceRangeDto getPriceRangeByCategoryAndBrand(Long categoryId, Long brandId);

    List<ProductPreviewDto> getRandomProductsByCategory(Long categoryId);

    PriceRangeDto getPriceRangeBySearchTerm(String searchTerm);


    List<ProductSummary> getProductSuggestions(String searchTerm);

    PaginatedResponse<ProductPreviewDto> getProductSearchResults (String searchTerm, BigDecimal minPrice,
                                                          BigDecimal maxPrice, int page);

    ProductDetailDto getProductById(Long id);

    List<ProductPreviewDto> getProductsByIds(List<Long> ids);


    List<ProductPreviewDto> recommendProducts(List<Long> ids);


}