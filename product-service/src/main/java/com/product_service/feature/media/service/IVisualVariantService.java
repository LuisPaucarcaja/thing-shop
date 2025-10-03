package com.product_service.feature.media.service;

import com.product_service.feature.media.dto.VisualVariantDto;

import java.util.List;

public interface IVisualVariantService {

    List<VisualVariantDto> getByIds(List<Long> ids);
}
