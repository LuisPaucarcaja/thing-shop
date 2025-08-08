package ProductMicroservice.feature.media.mapper;

import ProductMicroservice.feature.media.dto.VisualVariantDto;
import ProductMicroservice.feature.media.entity.VisualVariant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" , uses = ProductMediaMapper.class)
public interface VisualVariantMapper {

    VisualVariantDto toDto(VisualVariant visualVariant);
}
