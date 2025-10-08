package com.product_service.feature.product.mapper;

import com.product_service.feature.media.entity.ProductMedia;
import com.product_service.feature.media.mapper.ProductMediaMapper;
import com.product_service.feature.product.dto.ProductDetailDto;
import com.product_service.feature.product.dto.ProductPreviewDto;
import com.product_service.feature.product.dto.ProductSummary;
import com.product_service.feature.product.entity.Product;
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
        if (product == null || product.getGenericMediaList() == null) {
            return null;
        }

        return product.getGenericMediaList()
                .stream()
                .filter(ProductMedia::getIsPrimary)
                .map(ProductMedia::getUrl)
                .findFirst()
                .orElse(null);
    }



}

