package ProductMicroservice.feature.variant.service;

import ProductMicroservice.feature.variant.dto.VariantDetailDTO;
import ProductMicroservice.feature.variant.dto.VariantDTO;
import ProductMicroservice.feature.variant.dto.VariantsResponseDto;

import java.util.List;

public interface IVariantService {

    VariantDTO getById(Long id);

    List<VariantDetailDTO> getByIdIn(List<Long> ids);

    VariantsResponseDto getByProductId(Long productId);

}
