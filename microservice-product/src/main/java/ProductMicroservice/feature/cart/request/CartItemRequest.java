package ProductMicroservice.feature.cart.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {
    private Long id;

    @NotNull
    private Long variantId;

    @Min(value = 1)
    private int quantity;

}
