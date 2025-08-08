package ProductMicroservice.feature.product.mapper;

import ProductMicroservice.feature.product.dto.CategoryDto;
import ProductMicroservice.feature.product.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "parentCategoryId", source = "parentCategory.id")
    CategoryDto toDto(Category category);
}
