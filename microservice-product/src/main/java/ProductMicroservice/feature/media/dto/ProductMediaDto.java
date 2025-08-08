package ProductMicroservice.feature.media.dto;

import ProductMicroservice.feature.media.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductMediaDto {
    private Long id;
    private String url;

    private MediaType type;

    private Boolean isPrimary;
}
