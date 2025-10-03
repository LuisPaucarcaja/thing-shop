package com.product_service.feature.product.mapper;

import com.product_service.feature.product.dto.CategoryDto;
import com.product_service.feature.product.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "parentCategoryId", source = "parentCategory.id")
    CategoryDto toDto(Category category);
}
