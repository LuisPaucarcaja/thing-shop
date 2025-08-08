package ProductMicroservice.feature.media.mapper;


import ProductMicroservice.feature.media.dto.ProductMediaDto;
import ProductMicroservice.feature.media.entity.ProductMedia;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMediaMapper {

    ProductMediaDto toDto(ProductMedia productMedia);
}
