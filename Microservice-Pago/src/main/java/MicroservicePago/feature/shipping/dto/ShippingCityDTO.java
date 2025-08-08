package MicroservicePago.feature.shipping.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingCityDTO {


    private Long id;

    private String name;

    private List<ShippingDistrictDTO> shippingDistricts;
}
