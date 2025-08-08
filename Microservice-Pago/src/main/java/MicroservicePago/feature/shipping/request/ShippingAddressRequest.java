package MicroservicePago.feature.shipping.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ShippingAddressRequest {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String addressLine1;

    @NotNull
    @Size(max = 80)
    private String addressLine2;

    @NotNull
    @Pattern(regexp = "\\d{3,10}")
    private String postalCode;

    @NotNull
    private Long shippingDistrictId;

    private Double latitude;
    private Double longitude;
}
