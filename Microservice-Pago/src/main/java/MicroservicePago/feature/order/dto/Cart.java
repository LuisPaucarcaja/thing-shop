package MicroservicePago.feature.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Cart {

    private Long id;

    private List<CartItem> cartItems;
}
