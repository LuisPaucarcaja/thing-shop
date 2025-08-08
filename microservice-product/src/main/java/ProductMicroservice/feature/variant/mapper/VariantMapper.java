package ProductMicroservice.feature.variant.mapper;

import ProductMicroservice.feature.variant.dto.*;
import ProductMicroservice.feature.media.entity.ProductMedia;
import ProductMicroservice.feature.variant.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface VariantMapper {



    @Mapping(target = "productId" , source = "product.id")
    @Mapping(target = "name" , source = "product.name")
    @Mapping(target = "imageUrl", expression = "java(getPrimaryImageUrl(productVariant))")
    VariantDetailDTO toDetailDto(ProductVariant productVariant);


    default String getPrimaryImageUrl(ProductVariant productVariant) {
        if (productVariant.getVisualVariant() != null &&
                productVariant.getVisualVariant().getMediaList() != null &&
                !productVariant.getVisualVariant().getMediaList().isEmpty()) {

            return productVariant.getVisualVariant().getMediaList()
                    .stream()
                    .filter(ProductMedia::getIsPrimary)
                    .map(ProductMedia::getUrl)
                    .findFirst()
                    .orElse(null);
        }

        // Ya no se devuelve imagen de product, simplemente null
        return null;
    }


    @Mapping(target = "visualVariantId", source = "visualVariant.id")
    VariantDTO toDto(ProductVariant productVariant);



}
