package com.product_service.feature.product.dto;

import com.product_service.feature.media.dto.ProductMediaDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ProductDetailDto {

    private Long id;

    private String name;
    private String description;

    private BigDecimal price;

    private Double discount;

    private String brand;

    private Double qualification;

    private Date createdAt;

    private List<ProductMediaDto> genericMediaList;
}
