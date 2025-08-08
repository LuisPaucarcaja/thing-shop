package ProductMicroservice.feature.media.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisualVariantDto{

    private Long id;

    private Set<ProductMediaDto> mediaList;
}
