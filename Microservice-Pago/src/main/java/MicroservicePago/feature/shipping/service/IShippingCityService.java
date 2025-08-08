package MicroservicePago.feature.shipping.service;

import MicroservicePago.feature.shipping.dto.ShippingCityDTO;

import java.util.List;

public interface IShippingCityService {

    List<ShippingCityDTO> getActiveCitiesAndDistricts();
}
