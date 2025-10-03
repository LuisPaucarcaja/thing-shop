package com.product_service.feature.media.repository;

import com.product_service.feature.media.entity.VisualVariant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisualVariantRepository extends JpaRepository<VisualVariant, Long> {

    @EntityGraph(attributePaths = {
            "mediaList"
    })
    List<VisualVariant> findByIdIn(List<Long> ids);
}
