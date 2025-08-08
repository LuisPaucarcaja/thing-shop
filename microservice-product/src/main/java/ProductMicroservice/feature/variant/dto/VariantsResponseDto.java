package ProductMicroservice.feature.variant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VariantsResponseDto implements Serializable {
    private List<AttributeDto> attributes;
    private List<ProductVariantDto> variants;
}
