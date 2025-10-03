package com.product_service.feature.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {

    private Long id;

    private String name;

    private Long parentCategoryId;
}
