package MicroservicePago.feature.order.service.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import static MicroservicePago.common.constants.CacheConstants.GET_ORDERS_BY_USER;

@Service
public class OrderCacheService {

    @CacheEvict(value = GET_ORDERS_BY_USER, key = "#userId")
    public void evictOrdersByUser(Long userId) {}
}