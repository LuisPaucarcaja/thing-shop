package com.order_service.feature.shipping.mapper;


import com.order_service.feature.shipping.dto.ShippingCityDTO;
import com.order_service.feature.shipping.entity.ShippingCity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ShippingDistrictMapper.class)
public interface ShippingCityMapper {

    ShippingCityDTO toDto(ShippingCity shippingCity);
}
