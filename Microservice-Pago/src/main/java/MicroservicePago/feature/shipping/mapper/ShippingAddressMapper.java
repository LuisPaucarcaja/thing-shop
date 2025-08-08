package MicroservicePago.feature.shipping.mapper;

import MicroservicePago.feature.shipping.dto.ShippingAddressDTO;
import MicroservicePago.feature.shipping.entity.ShippingAddress;
import MicroservicePago.feature.shipping.request.ShippingAddressRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShippingAddressMapper {
    @Mapping(source = "shippingDistrict", target = "shippingDistrict")
    ShippingAddressDTO toDto(ShippingAddress shippingAddress);

    ShippingAddress toEntity(ShippingAddressRequest shippingAddressRequest);
}
