package com.Inventory_service.feature.inventory.service;

import com.Inventory_service.feature.inventory.dto.StockLevel;

import java.util.List;

public interface InventoryService {

    List<StockLevel> getAvailableStockLevelsByVariantIds(List<Long> ids);

}
