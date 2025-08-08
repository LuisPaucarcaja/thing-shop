package MicroservicePago.feature.order.mapper;

import MicroservicePago.feature.order.dto.OrderDto;
import MicroservicePago.feature.order.entity.Order;
import MicroservicePago.feature.shipping.entity.ShippingAddress;
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
