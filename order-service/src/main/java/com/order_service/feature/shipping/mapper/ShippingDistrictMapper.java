package com.order_service.feature.shipping.mapper;

import com.order_service.feature.shipping.dto.ShippingDistrictDTO;
import com.order_service.feature.shipping.entity.ShippingDistrict;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShippingDistrictMapper {
    ShippingDistrictDTO toDto(ShippingDistrict shippingDistrict);
}
