package com.product_service.feature.product.service.impl;

import com.product_service.common.dto.PaginatedResponse;
import com.product_service.common.exception.ResourceNotFoundException;
import com.product_service.feature.product.dto.ProductDetailDto;
import com.product_service.feature.product.dto.ProductPreviewDto;
import com.product_service.feature.product.entity.Product;
import com.product_service.feature.product.mapper.ProductMapper;
import com.product_service.feature.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImpUnitTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImp productServiceImp;

    @Test
    void givenExistingProduct_whenGetProductById_thenReturnDto() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        ProductDetailDto dto = new ProductDetailDto();
        dto.setId(1L);
        dto.setName("Test Product");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDetailDto(product)).thenReturn(dto);

        // Act
        ProductDetailDto result = productServiceImp.getProductById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Product", result.getName());
        verify(productRepository, times(1)).findById(1L);
        verify(productMapper, times(1)).toDetailDto(product);
    }

    @Test
    void givenNonExistingProduct_whenGetProductById_thenThrowException() {
        // Arrange
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> productServiceImp.getProductById(99L));
    }


    @Test
    void getByCategoryAndBrandAndPriceRange_ShouldReturnPaginatedResponse(){
        // Arrange
        Page<ProductPreviewDto> mockPage = new PageImpl<>(
                List.of(new ProductPreviewDto(1L, "test", BigDecimal.TEN, "url", null,
                        5.0, LocalDateTime.now()))
        );

        when(productRepository.searchByCategoryWithFilters(eq(1L), eq(2L), any(BigDecimal.class),
                any(BigDecimal.class), any(Pageable.class))).thenReturn(mockPage);

        // Act

        PaginatedResponse<ProductPreviewDto> response = productServiceImp.getByCategoryAndBrandAndPriceRange(
                1L, 2L, BigDecimal.valueOf(100), BigDecimal.valueOf(500), 0
        );

        // Assert

        assertEquals(1, response.getContent().size());
        assertEquals(0, response.getPageNumber());
        assertEquals(1, response.getTotalPages());

    }
}
