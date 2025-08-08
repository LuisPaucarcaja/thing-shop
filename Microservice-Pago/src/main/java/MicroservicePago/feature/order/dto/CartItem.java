package MicroservicePago.feature.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItem {

    private Long id;

    private Long variantId;

    private BigDecimal price;

    private int quantity;

}
