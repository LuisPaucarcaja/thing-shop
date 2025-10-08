package com.product_service.feature.product.repository;

import com.product_service.feature.product.dto.PriceRangeDto;
import com.product_service.feature.product.dto.ProductPreviewDto;
import com.product_service.feature.product.dto.ProductSummary;
import com.product_service.feature.product.entity.Brand;
import com.product_service.feature.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ============================
    // ========== ENTITY ==========
    // ============================

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.genericMediaList gm " +
            "JOIN FETCH p.categories WHERE p.id =:id AND gm.isPrimary = true")
    Optional<Product> findByProductId(@Param("id") Long id);

    @Query("SELECT p.id FROM Product p JOIN p.categories c WHERE c.id = :categoryId")
    Page<Long> findIdsByCategory(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.genericMediaList gm WHERE p.id IN :ids AND gm.isPrimary = true")
    List<Product> findByIdsWithPrimaryMedia(@Param("ids") List<Long> ids);


    @Query("SELECT DISTINCT p.brand FROM Product p JOIN p.categories c WHERE c.id = :categoryId")
    List<Brand> findBrandsByCategoryId(@Param("categoryId") Long categoryId);


    // ============================
    // ========= PREVIEWS =========
    // ============================

    String PRODUCT_PREVIEW_SELECT = """
        SELECT DISTINCT new com.product_service.feature.product.dto.ProductPreviewDto(
            p.id, p.name, p.price,
            (SELECT pm.url FROM p.genericMediaList pm WHERE pm.isPrimary = true),
            p.discount,
            p.qualification,
            p.createdAt
        )
        FROM Product p
    """;

    @Query(PRODUCT_PREVIEW_SELECT + """
           JOIN p.categories c
           WHERE c.id = :categoryId
             AND (:brandId IS NULL OR p.brand.id = :brandId)
             AND (:minPrice IS NULL OR p.price >= :minPrice)
             AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        """)
    Page<ProductPreviewDto> searchByCategoryWithFilters(
            @Param("categoryId") Long categoryId,
            @Param("brandId") Long brandId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);

    @Query(PRODUCT_PREVIEW_SELECT + " WHERE p.id IN :ids")
    List<ProductPreviewDto> findProductPreviewsByIds(@Param("ids") List<Long> ids);

    @Query(PRODUCT_PREVIEW_SELECT + """
           JOIN p.categories c
           WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
           OR LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
             AND (:minPrice IS NULL OR p.price >= :minPrice)
             AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        """)
    Page<ProductPreviewDto> searchProductByNameAndPriceRange(
            @Param("searchTerm") String searchTerm,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);


    // ============================
    // ======= AUTOCOMPLETE =======
    // ============================

    @Query("""
    SELECT DISTINCT new com.product_service.feature.product.dto.ProductSummary(
        p.id, p.name
    )
    FROM Product p
    JOIN p.categories c
    WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
       OR LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
    """)
    Page<ProductSummary> findProductSummariesByTerm(
            @Param("searchTerm") String searchTerm,
            Pageable pageable);



    // ============================
    // ======== PRICE RANGE =======
    // ============================

    @Query("""
           SELECT new com.product_service.feature.product.dto.PriceRangeDto(
               MIN(p.price),
               MAX(p.price)
           )
           FROM Product p
           JOIN p.categories c
           WHERE c.id = :categoryId
             AND (:brandId IS NULL OR p.brand.id = :brandId)
        """)
    PriceRangeDto findPriceRangeByCategoryAndBrand(
            @Param("categoryId") Long categoryId,
            @Param("brandId") Long brandId);

    @Query("""
           SELECT new com.product_service.feature.product.dto.PriceRangeDto(
               MIN(p.price),
               MAX(p.price)
           )
           FROM Product p
           JOIN p.categories c
           WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
              OR LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
        """)
    PriceRangeDto findPriceRangeBySearchTerm(@Param("searchTerm") String searchTerm);


    // ============================
    // ===== RANDOM PRODUCTS ======
    // ============================

    @Query(value = """
        SELECT p.id FROM products p
        JOIN product_categories pc ON p.id = pc.product_id
        WHERE pc.category_id = :categoryId
        ORDER BY RANDOM()
        LIMIT :limit
        """, nativeQuery = true)
    List<Long> findRandomProductIdsByCategory(@Param("categoryId") Long categoryId, @Param("limit") int limit);

}
