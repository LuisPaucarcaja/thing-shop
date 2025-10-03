package com.product_service.feature.product.builders;

import com.product_service.feature.product.entity.Brand;

public class BrandTestBuilder {

    private String name = "Adidas";

    public static BrandTestBuilder aBrand() {
        return new BrandTestBuilder();
    }

    public BrandTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Brand build() {
        return new Brand(null, name);
    }
}
