package ProductMicroservice.feature.variant.dto;

import ProductMicroservice.feature.variant.entity.ProductVariantAttribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VariantDetailDTO implements Serializable {

    private Long id;

    private Long productId;

    private String name;

    private BigDecimal price;

    private String imageUrl;

    private List<ProductVariantAttribute> variantAttributes;
}
