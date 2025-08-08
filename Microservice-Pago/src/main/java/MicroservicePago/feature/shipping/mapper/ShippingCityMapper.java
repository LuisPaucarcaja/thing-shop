package MicroservicePago.feature.shipping.mapper;


import MicroservicePago.feature.shipping.dto.ShippingCityDTO;
import MicroservicePago.feature.shipping.entity.ShippingCity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ShippingDistrictMapper.class)
public interface ShippingCityMapper {

    ShippingCityDTO toDto(ShippingCity shippingCity);
}
