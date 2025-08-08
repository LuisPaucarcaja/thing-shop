package ProductMicroservice.feature.media.service;

import ProductMicroservice.feature.media.dto.VisualVariantDto;

import java.util.List;

public interface IVisualVariantService {

    List<VisualVariantDto> getByIds(List<Long> ids);
}
