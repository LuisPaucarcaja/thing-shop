package com.product_service.feature.media.repository;

import com.product_service.feature.media.entity.CoverImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoverImageRepository extends JpaRepository<CoverImage, Long> {

    List<CoverImage> findByCoverGroup(int group);
}
