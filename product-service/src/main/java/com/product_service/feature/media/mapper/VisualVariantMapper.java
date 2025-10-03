package com.product_service.feature.media.mapper;

import com.product_service.feature.media.dto.VisualVariantDto;
import com.product_service.feature.media.entity.VisualVariant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" , uses = ProductMediaMapper.class)
public interface VisualVariantMapper {

    VisualVariantDto toDto(VisualVariant visualVariant);
}
