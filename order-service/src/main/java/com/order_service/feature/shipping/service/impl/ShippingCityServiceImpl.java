package com.order_service.feature.shipping.service.impl;

import com.order_service.feature.shipping.repository.ShippingCityRepository;
import com.order_service.feature.shipping.service.IShippingCityService;
import com.order_service.feature.shipping.dto.ShippingCityDTO;
import com.order_service.feature.shipping.mapper.ShippingCityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.order_service.common.constants.CacheConstants.GET_ALL_SHIPPING_CITIES;

@RequiredArgsConstructor
@Service
public class ShippingCityServiceImpl implements IShippingCityService {

    private final ShippingCityRepository shippingCityRepository;
    private final ShippingCityMapper shippingCityMapper;
    @Override
    @Cacheable(GET_ALL_SHIPPING_CITIES)
    public List<ShippingCityDTO> getActiveCitiesAndDistricts() {
        return shippingCityRepository.findActiveCitiesAndDistricts().stream().map(shippingCityMapper::toDto).toList();
    }
}
