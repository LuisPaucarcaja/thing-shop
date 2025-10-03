package com.order_service.feature.order.service.cache;

import com.order_service.common.constants.CacheConstants;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class OrderCacheService {

    @CacheEvict(value = CacheConstants.GET_ORDERS_BY_USER, key = "#userId")
    public void evictOrdersByUser(Long userId) {}
}