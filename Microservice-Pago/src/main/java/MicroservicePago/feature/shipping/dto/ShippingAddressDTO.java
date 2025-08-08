package MicroservicePago.feature.shipping.dto;

import MicroservicePago.feature.shipping.entity.ShippingDistrict;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ShippingAddressDTO {

    private Long id;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
    private ShippingDistrict shippingDistrict;
}
