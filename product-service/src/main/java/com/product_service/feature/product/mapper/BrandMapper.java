package com.product_service.feature.product.mapper;

import com.product_service.feature.product.dto.BrandDto;
import com.product_service.feature.product.entity.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    BrandDto toBrandDto(Brand brand);
}
