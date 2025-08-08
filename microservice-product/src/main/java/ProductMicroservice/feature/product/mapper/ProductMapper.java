package ProductMicroservice.feature.product.mapper;

import ProductMicroservice.feature.media.entity.ProductMedia;
import ProductMicroservice.feature.media.mapper.ProductMediaMapper;
import ProductMicroservice.feature.product.dto.ProductDetailDto;
import ProductMicroservice.feature.product.dto.ProductPreviewDto;
import ProductMicroservice.feature.product.dto.ProductSummary;
import ProductMicroservice.feature.product.entity.Product;
import ProductMicroservice.feature.variant.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ProductMediaMapper.class)
public interface ProductMapper {

    @Mapping(target = "brand", source = "brand.name")
    @Mapping(target = "genericMediaList", source = "genericMediaList")
    ProductDetailDto toDetailDto(Product product);


    @Mapping(target = "imageUrl", expression = "java(getPrimaryImageUrl(product))")
    ProductPreviewDto toPreviewDto(Product product);

    ProductSummary toSummaryDto(Product product);

    default String getPrimaryImageUrl(Product product) {
        return product.getGenericMediaList()
                .stream()
                .filter(ProductMedia::getIsPrimary)
                .map(ProductMedia::getUrl)
                .findFirst()
                .orElse(null);
    }


}

