package ProductMicroservice.feature.media.service.impl;

import ProductMicroservice.feature.media.dto.VisualVariantDto;
import ProductMicroservice.feature.media.mapper.VisualVariantMapper;
import ProductMicroservice.feature.media.repository.VisualVariantRepository;
import ProductMicroservice.feature.media.service.IVisualVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static ProductMicroservice.common.constants.CacheConstants.GET_VISUAL_VARIANTS_BY_IDS;

@RequiredArgsConstructor
@Service
public class VisualVariantServiceImpl implements IVisualVariantService {

    private final VisualVariantRepository visualVariantRepository;
    private final VisualVariantMapper visualVariantMapper;

    @Override
    @Cacheable(GET_VISUAL_VARIANTS_BY_IDS)
        public List<VisualVariantDto> getByIds(List<Long> ids) {
        return visualVariantRepository.findByIdIn(ids).stream()
                .map(visualVariantMapper::toDto).toList();

    }

}
