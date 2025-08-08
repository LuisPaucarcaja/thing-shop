package ProductMicroservice.feature.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PriceRangeDto {

    private BigDecimal minPrice;

    private BigDecimal maxPrice;
}
