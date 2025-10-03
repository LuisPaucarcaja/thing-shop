package com.order_service.feature.shipping.util;

import com.order_service.feature.shipping.entity.ShippingCity;
import com.order_service.feature.shipping.entity.ShippingDistrict;

public class ShippingDistrictBuilder {

    private Long id;
    private String name = "Default District";

    private ShippingCity shippingCity;
    public static ShippingDistrictBuilder aDistrict() {
        return new ShippingDistrictBuilder();
    }

    public ShippingDistrictBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ShippingDistrictBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ShippingDistrictBuilder withCity(ShippingCity city){
        this.shippingCity = city;
        return this;
    }

    public ShippingDistrict build() {
        ShippingDistrict district = new ShippingDistrict();
        district.setId(id);
        district.setName(name);
        return district;
    }
}
