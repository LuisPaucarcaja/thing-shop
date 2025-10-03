package com.product_service.feature.media.service.impl;

import com.product_service.feature.media.dto.VisualVariantDto;
import com.product_service.feature.media.mapper.VisualVariantMapper;
import com.product_service.feature.media.repository.VisualVariantRepository;
import com.product_service.feature.media.service.IVisualVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.product_service.common.constants.CacheConstants.GET_VISUAL_VARIANTS_BY_IDS;

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
