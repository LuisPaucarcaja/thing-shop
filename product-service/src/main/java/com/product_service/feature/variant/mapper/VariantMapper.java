package com.product_service.feature.variant.mapper;

import com.product_service.feature.media.entity.ProductMedia;
import com.product_service.feature.variant.entity.ProductVariant;
import com.product_service.feature.variant.dto.VariantDTO;
import com.product_service.feature.variant.dto.VariantDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface VariantMapper {



    @Mapping(target = "productId" , source = "product.id")
    @Mapping(target = "name" , source = "product.name")
    @Mapping(target = "imageUrl", expression = "java(getPrimaryImageUrl(productVariant))")
    VariantDetailDTO toDetailDto(ProductVariant productVariant);


    default String getPrimaryImageUrl(ProductVariant productVariant) {

        if (productVariant.getVisualVariant() != null) {

            return  productVariant.getVisualVariant().getMediaList()
                    .stream()
                    .filter(m -> Boolean.TRUE.equals(m.getIsPrimary()))
                    .map(ProductMedia::getUrl)
                    .findFirst()
                    .orElse(null);

        }
        return null;

    }



    @Mapping(target = "visualVariantId", source = "visualVariant.id")
    VariantDTO toDto(ProductVariant productVariant);



}
