package com.order_service.feature.shipping.service;

import com.order_service.feature.shipping.dto.ShippingAddressDTO;
import com.order_service.feature.shipping.entity.ShippingAddress;
import com.order_service.feature.shipping.request.ShippingAddressRequest;

import java.util.List;

public interface IShippingAddressService {

    ShippingAddress getShippingAddressById(Long id);
    List<ShippingAddressDTO> getByUserId(Long userId);
    ShippingAddressDTO saveShippingAddress(Long userId, ShippingAddressRequest shippingAddressRequest);

    ShippingAddressDTO updateShippingAddress(Long id, Long userId, ShippingAddressRequest shippingAddressRequest);
}
