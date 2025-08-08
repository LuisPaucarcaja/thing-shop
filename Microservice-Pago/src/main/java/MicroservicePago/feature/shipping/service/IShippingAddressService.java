package MicroservicePago.feature.shipping.service;

import MicroservicePago.feature.shipping.dto.ShippingAddressDTO;
import MicroservicePago.feature.shipping.entity.ShippingAddress;
import MicroservicePago.feature.shipping.request.ShippingAddressRequest;

import java.util.List;

public interface IShippingAddressService {

    ShippingAddress getShippingAddressById(Long id);
    List<ShippingAddressDTO> getByUserId(Long userId);
    ShippingAddressDTO saveShippingAddress(Long userId, ShippingAddressRequest shippingAddressRequest);

    ShippingAddressDTO updateShippingAddress(Long id, Long userId, ShippingAddressRequest shippingAddressRequest);
}
