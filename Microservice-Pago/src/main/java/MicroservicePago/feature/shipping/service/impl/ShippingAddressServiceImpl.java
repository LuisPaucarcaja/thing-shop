package MicroservicePago.feature.shipping.service.impl;

import MicroservicePago.feature.shipping.dto.ShippingAddressDTO;
import MicroservicePago.feature.shipping.mapper.ShippingAddressMapper;
import MicroservicePago.feature.shipping.repository.ShippingAddressRepository;
import MicroservicePago.feature.shipping.repository.ShippingDistrictRepository;
import MicroservicePago.feature.shipping.request.ShippingAddressRequest;
import MicroservicePago.feature.shipping.entity.ShippingAddress;
import MicroservicePago.feature.shipping.entity.ShippingDistrict;
import MicroservicePago.common.exception.ResourceNotFoundException;
import MicroservicePago.common.exception.UnauthorizedAccessException;
import MicroservicePago.feature.shipping.service.IShippingAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import static MicroservicePago.common.constants.CacheConstants.GET_SHIPPING_DISTRICT_BY_ID;
import static MicroservicePago.common.constants.CacheConstants.GET_USER_ADDRESSES;
import static MicroservicePago.common.constants.ErrorMessageConstants.UNAUTHORIZED_ACCESS;
import static MicroservicePago.common.constants.GeneralConstants.FIELD_ID;

@RequiredArgsConstructor
@Service
public class ShippingAddressServiceImpl implements IShippingAddressService {

    private final ShippingAddressRepository shippingAddressRepository;
    private final ShippingAddressMapper shippingAddressMapper;
    private final ShippingDistrictRepository shippingDistrictRepository;

    @Override
    public ShippingAddress getShippingAddressById(Long id){
        return  shippingAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ShippingAddress.class.getSimpleName(),FIELD_ID, id));
    }

    @Override
    @Cacheable(GET_USER_ADDRESSES)
    public List<ShippingAddressDTO> getByUserId(Long userId) {
        return shippingAddressRepository.findByUserId(userId).stream()
                .map(shippingAddressMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(value = GET_USER_ADDRESSES, key = "#userId")
    public ShippingAddressDTO saveShippingAddress(Long userId, ShippingAddressRequest shippingAddressRequest) {
        ShippingDistrict shippingDistrict = getShippingDistrictById(shippingAddressRequest.getShippingDistrictId());

        ShippingAddress shippingAddress = shippingAddressMapper.toEntity(shippingAddressRequest);
        shippingAddress.setUserId(userId);
        shippingAddress.setShippingDistrict(shippingDistrict);
        return shippingAddressMapper.toDto(shippingAddressRepository.save(shippingAddress));
    }



    @Override
    @Transactional
    @CacheEvict(value = GET_USER_ADDRESSES, key = "#userId")
    public ShippingAddressDTO updateShippingAddress(Long id, Long userId, ShippingAddressRequest addressRequest) {

        ShippingAddress existingAddress = getShippingAddressById(id);

        // Check userId compatibility
        if (!existingAddress.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS);
        }

        ShippingDistrict shippingDistrict = getShippingDistrictById(addressRequest.getShippingDistrictId());

        // Assign missing properties
        ShippingAddress updatedAddress = shippingAddressMapper.toEntity(addressRequest);
        updatedAddress.setId(id);
        updatedAddress.setShippingDistrict(shippingDistrict);
        updatedAddress.setUserId(userId);

        return shippingAddressMapper.toDto(shippingAddressRepository.save(updatedAddress));
    }

    @Cacheable(GET_SHIPPING_DISTRICT_BY_ID)
    private ShippingDistrict getShippingDistrictById(Long id){
        return shippingDistrictRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ShippingDistrict.class.getSimpleName(),FIELD_ID, id));
    }


}
