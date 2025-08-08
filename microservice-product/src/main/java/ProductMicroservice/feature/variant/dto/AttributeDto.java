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
public class AttributeDto implements Serializable {
    private Long id;
    private String name;
    private boolean isVisual;
    private List<AttributeValueDto> values;
}
