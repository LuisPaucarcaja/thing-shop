package MicroservicePago.feature.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OutOfStockItem {
    private Long variantId;
    private int requestedQuantity;
    private int availableQuantity;

}
