package MicroservicePago.feature.order.client;

import MicroservicePago.feature.order.dto.StockLevel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-inventory", url = "${gateway.url}/api/inventories" )
public interface InventoryClient {


    @GetMapping
    List<StockLevel> getInventoriesByVariantIds(@RequestParam List<Long> variantIds);



}
