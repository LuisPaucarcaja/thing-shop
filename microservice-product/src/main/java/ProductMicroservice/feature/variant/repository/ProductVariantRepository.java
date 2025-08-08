package ProductMicroservice.feature.variant.repository;

import ProductMicroservice.feature.variant.entity.ProductVariant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {


    @EntityGraph(attributePaths = {
            "product",
            "variantAttributes.attributeValue.attribute",
            "visualVariant.mediaList",
    })
    List<ProductVariant> findByIdIn(List<Long> ids);

    @EntityGraph(attributePaths = {
            "variantAttributes.attributeValue.attribute",
            "visualVariant.mediaList",
    })
    List<ProductVariant> findByProductId(Long productId);


}
