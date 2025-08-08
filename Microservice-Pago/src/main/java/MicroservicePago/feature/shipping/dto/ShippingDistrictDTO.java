package MicroservicePago.feature.shipping.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDistrictDTO {

    private Long id;

    private String name;

    private BigDecimal shippingPrice;
}
