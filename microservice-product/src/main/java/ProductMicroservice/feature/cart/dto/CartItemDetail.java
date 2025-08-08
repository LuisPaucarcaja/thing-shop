package ProductMicroservice.feature.cart.dto;

import ProductMicroservice.feature.variant.dto.VariantDetailDTO;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CartItemDetail {


    private Long id;

    private VariantDetailDTO productVariant;
    private int quantity;

}
