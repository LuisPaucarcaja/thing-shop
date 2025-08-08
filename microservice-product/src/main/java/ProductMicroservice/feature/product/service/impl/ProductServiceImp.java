package ProductMicroservice.feature.product.service.impl;

import ProductMicroservice.common.exception.ResourceNotFoundException;
import ProductMicroservice.feature.product.dto.PriceRangeDto;
import ProductMicroservice.feature.product.dto.ProductDetailDto;
import ProductMicroservice.feature.product.dto.ProductPreviewDto;
import ProductMicroservice.feature.product.dto.ProductSummary;
import ProductMicroservice.feature.product.mapper.ProductMapper;
import ProductMicroservice.feature.product.entity.Product;
import ProductMicroservice.common.dto.PaginatedResponse;
import ProductMicroservice.feature.product.repository.ProductRepository;
import ProductMicroservice.feature.product.service.IProductService;
import ProductMicroservice.feature.product.service.recommendation.ProductRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static ProductMicroservice.common.constants.CacheConstants.*;
import static ProductMicroservice.common.constants.GeneralConstants.*;


@RequiredArgsConstructor
@Service
public class ProductServiceImp implements IProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private final ProductRecommendationService recommendationService;

    @Override
    @Cacheable(GET_PRODUCT_BY_ID)
    public ProductDetailDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDetailDto).
                orElseThrow(() -> new ResourceNotFoundException(Product.class.getSimpleName(), FIELD_ID, id));
    }


    @Override
    @Cacheable(GET_PRODUCTS_BY_CATEGORY_BRAND_PRICE)
    public PaginatedResponse<ProductPreviewDto> getByCategoryAndBrandAndPriceRange(Long categoryId, Long brandId,
                                                                                   BigDecimal minPrice,
                                                                                   BigDecimal maxPrice, int page) {

        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);
        Page<ProductPreviewDto> productsPage = productRepository.searchByCategoryWithFilters(categoryId, brandId,
                minPrice, maxPrice, pageable);

        return new PaginatedResponse<>(productsPage);
    }

    @Override
    @Cacheable(GET_PRICE_RANGE_BY_CATEGORY_BRAND)
    public PriceRangeDto getPriceRangeByCategoryAndBrand(Long categoryId, Long brandId) {
        return productRepository.findPriceRangeByCategoryAndBrand(categoryId, brandId);
    }

    @Override
    @Cacheable(GET_RANDOM_PRODUCTS_BY_CATEGORY)
    public List<ProductPreviewDto> getRandomProductsByCategory(Long categoryId) {

        List<Long> randomIds =  productRepository.findRandomProductIdsByCategory(categoryId, DEFAULT_PRODUCT_LIMIT);

        return getProductsByIds(randomIds);
    }


    @Override
    @Cacheable(GET_PRODUCTS_SUGGESTIONS)
    public List<ProductSummary> getProductSuggestions(String searchTerm) {
        Page<ProductSummary> productPage = productRepository.findProductSummariesByTerm(searchTerm,
                PageRequest.of(0, DEFAULT_PRODUCT_LIMIT));
        return productPage.getContent();
    }

    @Override
    @Cacheable(GET_PRODUCTS_BY_SEARCH_TERM)
    public PaginatedResponse<ProductPreviewDto> getProductSearchResults(String searchTerm, BigDecimal minPrice,
                                                                BigDecimal maxPrice, int page) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        Page<ProductPreviewDto> productPage = productRepository.searchProductByNameAndPriceRange(
                searchTerm, minPrice, maxPrice, pageable
        );


        return new PaginatedResponse<>(productPage);
    }


    @Override
    @Cacheable(GET_PRICE_RANGE_BY_SEARCH_TERM)
    public PriceRangeDto getPriceRangeBySearchTerm(String searchTerm) {
        return productRepository.findPriceRangeBySearchTerm(searchTerm);
    }


    @Override
    @Cacheable(GET_PRODUCTS_BY_IDS)
    public List<ProductPreviewDto> getProductsByIds(List<Long> ids){
        return productRepository.findProductPreviewsByIds(ids);
    }

    @Override
    @Cacheable(GET_RECOMMENDED_PRODUCTS)
    public List<ProductPreviewDto> recommendProducts(List<Long> ids) {
        return recommendationService.recommendProducts(ids)
                .stream().map(productMapper::toPreviewDto).toList();

    }


}