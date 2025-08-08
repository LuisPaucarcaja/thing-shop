package ProductMicroservice.feature.variant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductVariantDto implements Serializable {
    private Long id;
    private BigDecimal price;
    private Long visualVariantId;
    private List<Long> attributeValueIds;
}
