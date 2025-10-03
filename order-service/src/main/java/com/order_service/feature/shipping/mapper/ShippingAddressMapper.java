package com.order_service.feature.shipping.mapper;

import com.order_service.feature.shipping.dto.ShippingAddressDTO;
import com.order_service.feature.shipping.entity.ShippingAddress;
import com.order_service.feature.shipping.request.ShippingAddressRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShippingAddressMapper {
    @Mapping(source = "shippingDistrict", target = "shippingDistrict")
    ShippingAddressDTO toDto(ShippingAddress shippingAddress);

    ShippingAddress toEntity(ShippingAddressRequest shippingAddressRequest);
}
