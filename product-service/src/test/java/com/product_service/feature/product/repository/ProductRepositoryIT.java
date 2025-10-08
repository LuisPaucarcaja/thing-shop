package com.product_service.feature.product.repository;

import com.product_service.feature.media.entity.ProductMedia;
import com.product_service.feature.media.enums.MediaType;
import com.product_service.feature.product.builders.ProductTestBuilder;
import com.product_service.feature.product.entity.Brand;
import com.product_service.feature.product.entity.Category;
import com.product_service.feature.product.entity.Product;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }


    @Test
    @Sql("/datasets/products_with_categories.sql")
    void findByIdWithMediaAndBrand_whenProductExists_returnsProductWithBrandAndMedia() {
        Optional<Product> found = productRepository.findByProductId(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("UltraBoost");
    }

    @Test
    void findByIdWithMediaAndBrand_whenProductDoesNotExist_returnsEmptyOptional() {
        Optional<Product> found = productRepository.findById(999L);

        assertThat(found).isNotPresent();
    }

    @Test
    void findByIdsWithMediaTest() {


        Brand brand = new Brand(null, "Adidas");
        entityManager.persist(brand);

        Product product = ProductTestBuilder.aProduct()
                .build();

        ProductMedia media = ProductMedia.builder()
                .url("http://example.com/ultraboost.jpg")
                .type(MediaType.IMAGE)
                .isPrimary(true)
                .product(product)
                .build();

        product.setGenericMediaList(Set.of(media));
        product.setBrand(brand);
        product.setName("Test");

        entityManager.persist(product);
        entityManager.flush();

        List<Product> productList = productRepository.findByIdsWithPrimaryMedia(List.of(1L));

        assertEquals(1, productList.size());
        Product result = productList.get(0);

        assertThat(result.getGenericMediaList()).extracting("url").contains("http://example.com/ultraboost.jpg");
    }






}