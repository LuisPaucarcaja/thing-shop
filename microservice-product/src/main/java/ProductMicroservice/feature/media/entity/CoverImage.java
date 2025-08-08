package ProductMicroservice.feature.media.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cover_images")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CoverImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private int coverGroup;

    @Column(name = "desktop_image_url")
    private String desktopImageUrl;

    @Column(name = "mobile_image_url")
    private String mobileImageUrl;

    private String desktopLink;

    private String mobileLink;
}
