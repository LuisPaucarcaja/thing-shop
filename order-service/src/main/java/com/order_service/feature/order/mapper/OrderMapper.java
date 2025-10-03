package com.order_service.feature.order.mapper;

import com.order_service.feature.order.dto.OrderDto;
import com.order_service.feature.order.entity.Order;
import com.order_service.feature.shipping.entity.ShippingAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "shippingAddress", expression = "java(getShippingAddress(order.getShippingAddress()))")
    @Mapping(target = "paymentMethod", source = "orderPayment.paymentMethod")
    OrderDto toDto(Order order);

    default String getShippingAddress(ShippingAddress shippingAddress) {
        return shippingAddress.getShippingDistrict().getShippingCity().getName()
                .concat(", ").concat(shippingAddress.getShippingDistrict().getName());
    }

}
