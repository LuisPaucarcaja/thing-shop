package com.order_service.feature.shipping.util;

import com.order_service.feature.shipping.entity.ShippingAddress;
import com.order_service.feature.shipping.entity.ShippingDistrict;

public class ShippingAddressBuilder {

    private Long id;
    private String addressLine1 = "Av. Default 123";
    private Long userId = 1L;
    private ShippingDistrict district;

    private Double latitude;
    private Double longitude;

    public static ShippingAddressBuilder anAddress() {
        return new ShippingAddressBuilder();
    }

    public ShippingAddressBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ShippingAddressBuilder withStreet(String addressLine1) {
        this.addressLine1 = addressLine1;
        return this;
    }

    public ShippingAddressBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public ShippingAddressBuilder withDistrict(ShippingDistrict district) {
        this.district = district;
        return this;
    }

    public ShippingAddressBuilder withLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public ShippingAddressBuilder withLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public ShippingAddress build() {
        ShippingAddress address = new ShippingAddress();
        address.setId(id);
        address.setAddressLine1(addressLine1);
        address.setUserId(userId);
        address.setShippingDistrict(district);
        return address;
    }
}
