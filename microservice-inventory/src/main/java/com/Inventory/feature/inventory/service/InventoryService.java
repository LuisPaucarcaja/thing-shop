package com.Inventory.feature.inventory.service;

import com.Inventory.feature.inventory.dto.StockLevel;

import java.util.List;

public interface InventoryService {

    List<StockLevel> getAvailableStockLevelsByVariantIds(List<Long> ids);

}
