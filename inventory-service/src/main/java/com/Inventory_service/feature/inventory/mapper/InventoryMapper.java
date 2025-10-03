package com.Inventory_service.feature.inventory.mapper;

import com.Inventory_service.feature.inventory.dto.StockLevel;
import com.Inventory_service.feature.inventory.entity.ProductInventory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    StockLevel toDto(ProductInventory productInventory);
}
