package ProductMicroservice.feature.variant.dto;

import ProductMicroservice.feature.variant.entity.ProductVariantAttribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VariantDTO {

    private Long id;

    private BigDecimal price;

    private Long visualVariantId;

    private List<ProductVariantAttribute> variantAttributes;
}
