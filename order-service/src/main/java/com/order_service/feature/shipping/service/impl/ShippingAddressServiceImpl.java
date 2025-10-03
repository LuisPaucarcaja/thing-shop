package com.order_service.feature.shipping.service.impl;

import com.order_service.feature.shipping.dto.ShippingAddressDTO;
import com.order_service.feature.shipping.mapper.ShippingAddressMapper;
import com.order_service.feature.shipping.repository.ShippingAddressRepository;
import com.order_service.feature.shipping.repository.ShippingDistrictRepository;
import com.order_service.feature.shipping.request.ShippingAddressRequest;
import com.order_service.feature.shipping.entity.ShippingAddress;
import com.order_service.feature.shipping.entity.ShippingDistrict;
import com.order_service.common.exception.ResourceNotFoundException;
import com.order_service.common.exception.UnauthorizedAccessException;
import com.order_service.feature.shipping.service.IShippingAddressService;
import com.order_service.common.constants.CacheConstants;
import com.order_service.common.constants.ErrorMessageConstants;
import com.order_service.common.constants.GeneralConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.order_service.common.constants.CacheConstants.GET_USER_ADDRESSES;

@RequiredArgsConstructor
@Service
public class ShippingAddressServiceImpl implements IShippingAddressService {

    private final ShippingAddressRepository shippingAddressRepository;
    private final ShippingAddressMapper shippingAddressMapper;
    private final ShippingDistrictRepository shippingDistrictRepository;

    @Override
    @Cacheable(GET_USER_ADDRESSES)
    public List<ShippingAddressDTO> getByUserId(Long userId) {
        return shippingAddressRepository.findByUserId(userId).stream()
                .map(shippingAddressMapper::toDto).collect(Collectors.toList());
    }


    @Override
    public ShippingAddress getShippingAddressById(Long id){
        return  shippingAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ShippingAddress.class.getSimpleName(),
                        GeneralConstants.FIELD_ID, id));
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
            throw new UnauthorizedAccessException(ErrorMessageConstants.UNAUTHORIZED_ACCESS);
        }

        ShippingDistrict shippingDistrict = getShippingDistrictById(addressRequest.getShippingDistrictId());

        // Assign missing properties
        ShippingAddress updatedAddress = shippingAddressMapper.toEntity(addressRequest);
        updatedAddress.setId(id);
        updatedAddress.setShippingDistrict(shippingDistrict);
        updatedAddress.setUserId(userId);

        return shippingAddressMapper.toDto(shippingAddressRepository.save(updatedAddress));
    }

    @Cacheable(CacheConstants.GET_SHIPPING_DISTRICT_BY_ID)
    private ShippingDistrict getShippingDistrictById(Long id){
        return shippingDistrictRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ShippingDistrict.class.getSimpleName(),
                        GeneralConstants.FIELD_ID, id));
    }


}
