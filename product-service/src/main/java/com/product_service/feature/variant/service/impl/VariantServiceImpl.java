package com.product_service.feature.variant.service.impl;

import com.product_service.feature.variant.dto.VariantDetailDTO;
import com.product_service.feature.variant.dto.VariantDTO;
import com.product_service.common.exception.ResourceNotFoundException;
import com.product_service.feature.variant.dto.VariantsResponseDto;
import com.product_service.feature.variant.mapper.VariantMapper;
import com.product_service.feature.variant.entity.ProductVariant;
import com.product_service.feature.variant.mapper.helper.VariantAssembler;
import com.product_service.feature.variant.repository.ProductVariantRepository;
import com.product_service.feature.variant.service.IVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.product_service.common.constants.CacheConstants.*;
import static com.product_service.common.constants.GeneralConstants.FIELD_ID;

@RequiredArgsConstructor
@Service
public class VariantServiceImpl implements IVariantService {

    private final ProductVariantRepository variantRepository;
    private final VariantMapper variantMapper;
    private final VariantAssembler variantAssembler;

    @Override
    public VariantDTO getById(Long variantId) {
        return variantRepository.findById(variantId).map(variantMapper::toDto).orElseThrow(() ->
                new ResourceNotFoundException(ProductVariant.class.getSimpleName(), FIELD_ID, variantId));
    }

    @Override
    @Cacheable(GET_VARIANTS_BY_IDS)
    public List<VariantDetailDTO> getByIdIn(List<Long> ids) {
        return variantRepository.findByIdIn(ids).stream()
                .map(variantMapper::toDetailDto).collect(Collectors.toList());
    }

    @Override
    @Cacheable(GET_VARIANTS_BY_PRODUCT)
    public VariantsResponseDto getByProductId(Long productId) {
        List<ProductVariant> variants = variantRepository.findByProductId(productId);
        return variantAssembler.toVariantsResponse(variants);
    }



}
