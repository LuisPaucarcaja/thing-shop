package ProductMicroservice.feature.variant.service.impl;

import ProductMicroservice.feature.variant.dto.VariantDetailDTO;
import ProductMicroservice.feature.variant.dto.VariantDTO;
import ProductMicroservice.common.exception.ResourceNotFoundException;
import ProductMicroservice.feature.variant.dto.VariantsResponseDto;
import ProductMicroservice.feature.variant.mapper.VariantMapper;
import ProductMicroservice.feature.variant.entity.ProductVariant;
import ProductMicroservice.feature.variant.mapper.helper.VariantAssembler;
import ProductMicroservice.feature.variant.repository.ProductVariantRepository;
import ProductMicroservice.feature.variant.service.IVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static ProductMicroservice.common.constants.CacheConstants.*;
import static ProductMicroservice.common.constants.GeneralConstants.FIELD_ID;

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
