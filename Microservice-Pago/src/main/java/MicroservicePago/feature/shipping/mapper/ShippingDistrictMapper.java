package MicroservicePago.feature.shipping.mapper;

import MicroservicePago.feature.shipping.dto.ShippingDistrictDTO;
import MicroservicePago.feature.shipping.entity.ShippingDistrict;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShippingDistrictMapper {
    ShippingDistrictDTO toDto(ShippingDistrict shippingDistrict);
}
