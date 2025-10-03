package com.product_service.feature.product.builders;

import com.product_service.feature.media.entity.ProductMedia;
import com.product_service.feature.media.enums.MediaType;
import com.product_service.feature.product.entity.Product;

public class ProductMediaTestBuilder {

    private String url = "http://example.com/ultraboost.jpg";

    private MediaType type = MediaType.IMAGE;

    private Boolean isPrimary = true;

    public static ProductMediaTestBuilder aProductMedia(){
        return new ProductMediaTestBuilder();
    }

    public ProductMedia build(Product product){
        return new ProductMedia(null, url, type, isPrimary, product, null);
    }
}
