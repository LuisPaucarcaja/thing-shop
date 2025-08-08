package ProductMicroservice.feature.product.mapper;

import ProductMicroservice.feature.product.dto.BrandDto;
import ProductMicroservice.feature.product.entity.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    BrandDto toBrandDto(Brand brand);
}
