package ProductMicroservice.feature.variant.mapper.helper;

import ProductMicroservice.feature.variant.dto.AttributeDto;
import ProductMicroservice.feature.variant.dto.AttributeValueDto;
import ProductMicroservice.feature.variant.dto.ProductVariantDto;
import ProductMicroservice.feature.variant.dto.VariantsResponseDto;
import ProductMicroservice.feature.variant.entity.Attribute;
import ProductMicroservice.feature.variant.entity.AttributeValue;
import ProductMicroservice.feature.variant.entity.ProductVariant;
import ProductMicroservice.feature.variant.entity.ProductVariantAttribute;
import ProductMicroservice.feature.variant.mapper.AttributeValueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VariantAssembler {

    private final AttributeValueMapper attributeValueMapper;

    public VariantsResponseDto toVariantsResponse(List<ProductVariant> productVariants) {
        Map<Attribute, Set<AttributeValue>> attributeMap = groupAttributes(productVariants);

        List<AttributeDto> attributes = mapAttributes(attributeMap);

        List<ProductVariantDto> variants = mapVariants(productVariants);

        return VariantsResponseDto.builder()
                .attributes(attributes)
                .variants(variants)
                .build();
    }

    private Map<Attribute, Set<AttributeValue>> groupAttributes(List<ProductVariant> productVariants) {

        Map<Attribute, Set<AttributeValue>> attributeMap = new LinkedHashMap<>();

        for (ProductVariant variant : productVariants) {
            for (ProductVariantAttribute attr : variant.getVariantAttributes()) {
                Attribute attribute = attr.getAttributeValue().getAttribute();
                attributeMap.computeIfAbsent(attribute, k -> new HashSet<>())
                        .add(attr.getAttributeValue());
            }
        }

        return attributeMap;

    }


    private List<AttributeDto> mapAttributes(Map<Attribute, Set<AttributeValue>> attributeMap) {
        return attributeMap.entrySet().stream().map(entry -> {
            List<AttributeValueDto> values = entry.getValue().stream()
                    .map(attributeValueMapper::toDto)
                    .toList();

            return AttributeDto.builder()
                    .id(entry.getKey().getId())
                    .name(entry.getKey().getName())
                    .isVisual(entry.getKey().isVisual())
                    .values(values)
                    .build();
        }).toList();

    }

    private List<ProductVariantDto> mapVariants(List<ProductVariant> productVariants) {
        return  productVariants.stream().map(variant -> {
            List<Long> valueIds = variant.getVariantAttributes().stream()
                    .map(attr -> attr.getAttributeValue().getId())
                    .toList();

            return ProductVariantDto.builder()
                    .id(variant.getId())
                    .price(variant.getPrice())
                    .visualVariantId(
                            variant.getVisualVariant() != null ? variant.getVisualVariant().getId() : null)
                    .attributeValueIds(valueIds)
                    .build();
        }).toList();

    }
}
