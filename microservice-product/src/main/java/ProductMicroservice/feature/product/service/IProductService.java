package ProductMicroservice.feature.product.service;

import ProductMicroservice.common.dto.PaginatedResponse;
import ProductMicroservice.feature.product.dto.PriceRangeDto;
import ProductMicroservice.feature.product.dto.ProductDetailDto;
import ProductMicroservice.feature.product.dto.ProductPreviewDto;
import ProductMicroservice.feature.product.dto.ProductSummary;

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