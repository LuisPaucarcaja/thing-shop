package com.product_service.feature.variant.service;

import com.product_service.feature.variant.dto.VariantDetailDTO;
import com.product_service.feature.variant.dto.VariantDTO;
import com.product_service.feature.variant.dto.VariantsResponseDto;

import java.util.List;

public interface IVariantService {

    VariantDTO getById(Long id);

    List<VariantDetailDTO> getByIdIn(List<Long> ids);

    VariantsResponseDto getByProductId(Long productId);

}
