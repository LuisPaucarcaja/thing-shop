package com.order_service.feature.shipping.service;

import com.order_service.feature.shipping.dto.ShippingCityDTO;

import java.util.List;

public interface IShippingCityService {

    List<ShippingCityDTO> getActiveCitiesAndDistricts();
}
