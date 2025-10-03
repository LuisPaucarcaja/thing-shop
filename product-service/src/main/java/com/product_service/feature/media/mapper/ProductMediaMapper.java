package com.product_service.feature.media.mapper;


import com.product_service.feature.media.dto.ProductMediaDto;
import com.product_service.feature.media.entity.ProductMedia;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMediaMapper {

    ProductMediaDto toDto(ProductMedia productMedia);
}
