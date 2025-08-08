package ProductMicroservice.feature.product.repository;

import ProductMicroservice.feature.product.dto.PriceRangeDto;
import ProductMicroservice.feature.product.dto.ProductPreviewDto;
import ProductMicroservice.feature.product.dto.ProductSummary;
import ProductMicroservice.feature.product.entity.Brand;
import ProductMicroservice.feature.product.entity.Product;
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

    @EntityGraph(attributePaths = {"genericMediaList", "brand"})
    Optional<Product> findById(@Param("id") Long id);

    @Query("SELECT p FROM Product p JOIN FETCH p.categories JOIN FETCH p.genericMediaList WHERE p.id IN :ids")
    List<Product> findByIdsWithCategories(@Param("ids") List<Long> ids);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.genericMediaList gm JOIN p.categories c WHERE c.id = :categoryId")
    Page<Product> findByCategory(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.genericMediaList " +
            "LEFT JOIN FETCH p.categories WHERE p IN :products")
    List<Product> fetchCollectionsForProducts(@Param("products") List<Product> products);

    @Query("SELECT DISTINCT p.brand FROM Product p JOIN p.categories c WHERE c.id = :categoryId")
    List<Brand> findBrandsByCategoryId(@Param("categoryId") Long categoryId);


    // ============================
    // ========= PREVIEWS =========
    // ============================

    String PRODUCT_PREVIEW_SELECT = """
        SELECT DISTINCT new ProductMicroservice.feature.product.dto.ProductPreviewDto(
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
    SELECT DISTINCT new ProductMicroservice.feature.product.dto.ProductSummary(
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
           SELECT new ProductMicroservice.feature.product.dto.PriceRangeDto(
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
           SELECT new ProductMicroservice.feature.product.dto.PriceRangeDto(
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
