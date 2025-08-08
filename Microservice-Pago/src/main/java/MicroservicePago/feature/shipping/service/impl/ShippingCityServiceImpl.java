package MicroservicePago.feature.shipping.service.impl;

import MicroservicePago.feature.shipping.repository.ShippingCityRepository;
import MicroservicePago.feature.shipping.service.IShippingCityService;
import MicroservicePago.feature.shipping.dto.ShippingCityDTO;
import MicroservicePago.feature.shipping.mapper.ShippingCityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static MicroservicePago.common.constants.CacheConstants.GET_ALL_SHIPPING_CITIES;

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
