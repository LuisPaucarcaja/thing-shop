package com.Inventory.feature.inventory.mapper;

import com.Inventory.feature.inventory.dto.StockLevel;
import com.Inventory.feature.inventory.entity.ProductInventory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    StockLevel toDto(ProductInventory productInventory);
}
