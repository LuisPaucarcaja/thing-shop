package ProductMicroservice.feature.cart.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CartDetail {

    private Long id;

    private Set<CartItemDetail> cartItems;

}
