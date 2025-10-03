package com.product_service.feature.variant.mapper;

import com.product_service.feature.variant.dto.AttributeValueDto;
import com.product_service.feature.variant.entity.AttributeValue;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttributeValueMapper {
    AttributeValueDto toDto(AttributeValue value);

}
