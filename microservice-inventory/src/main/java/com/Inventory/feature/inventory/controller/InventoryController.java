package com.Inventory.feature.inventory.controller;

import com.Inventory.feature.inventory.dto.StockLevel;
import com.Inventory.feature.inventory.service.InventoryService;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public List<StockLevel> getInventoriesByVariants( @RequestParam @Size(max = 50) List<@Positive Long> variantIds ){
        return inventoryService.getReducedInventories(variantIds);
    }


}
