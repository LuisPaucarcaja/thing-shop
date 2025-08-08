package ProductMicroservice.feature.variant.mapper;

import ProductMicroservice.feature.variant.dto.AttributeValueDto;
import ProductMicroservice.feature.variant.entity.AttributeValue;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttributeValueMapper {
    AttributeValueDto toDto(AttributeValue value);

}
