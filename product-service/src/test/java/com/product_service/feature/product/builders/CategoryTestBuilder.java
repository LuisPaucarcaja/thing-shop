package com.product_service.feature.product.builders;

import com.product_service.feature.product.entity.Category;

public class CategoryTestBuilder {

    private String name = "Shoes";

    public static CategoryTestBuilder aCategory(){
        return new CategoryTestBuilder();
    }

    public CategoryTestBuilder withName(String name){
        this.name = name;
        return this;
    }

    public Category build(){
        return new Category(null, name, null);
    }
}
