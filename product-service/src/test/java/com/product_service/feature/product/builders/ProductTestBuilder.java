package com.product_service.feature.product.builders;

import com.product_service.feature.media.entity.ProductMedia;
import com.product_service.feature.product.entity.Brand;
import com.product_service.feature.product.entity.Category;
import com.product_service.feature.product.entity.Product;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ProductTestBuilder {

    private String name = "Shoes adidas";
    private BigDecimal price = BigDecimal.TEN;
    private Brand brand = new Brand(null, "Adidas");
    private Double discount = 15.0;
    private Double qualification = 5.0;
    private Set<Category> categories = new HashSet<>();
    private Set<ProductMedia> genericMediaList = new HashSet<>();

    public static ProductTestBuilder aProduct() {
        return new ProductTestBuilder();
    }

    public ProductTestBuilder withCategory(Category category) {
        this.categories.add(category);
        return this;
    }

    public ProductTestBuilder withMediaList(ProductMedia productMedia){
        this.genericMediaList.add(productMedia);
        return this;
    }

    public ProductTestBuilder withBrand(Brand brand){
        this.brand = brand;
        return this;
    }

    public Product build() {
        Product product = Product.builder()
                .name(name)
                .price(price)
                .brand(brand)
                .discount(discount)
                .qualification(qualification)
                .categories(categories)
                .genericMediaList(genericMediaList)
                .build();
        return product;
    }
}
