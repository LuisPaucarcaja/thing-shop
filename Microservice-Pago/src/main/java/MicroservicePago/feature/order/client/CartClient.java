package MicroservicePago.feature.order.client;

import MicroservicePago.config.FeignClientConfig;
import MicroservicePago.feature.order.dto.Cart;
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
