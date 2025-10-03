package com.order_service.feature.order.client;

import com.order_service.config.FeignClientConfig;
import com.order_service.feature.order.dto.Cart;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "msvc-cart",
        url = "${gateway.url}/api/carts", configuration = FeignClientConfig.class
)
public interface CartClient {

    @GetMapping("/checkout")
    Cart getCartByUser();
}
