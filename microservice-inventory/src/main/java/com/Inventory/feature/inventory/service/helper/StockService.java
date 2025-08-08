package com.Inventory.feature.inventory.service.helper;

import com.Inventory.common.exceptioon.InsufficientStockException;
import com.Inventory.feature.inventory.entity.ProductInventory;
import com.Inventory.feature.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

import static com.Inventory.common.constants.GeneralConstants.INSUFFICIENT_STOCK_MESSAGE;

@RequiredArgsConstructor
@Service
public class StockService {

    private final InventoryRepository inventoryRepository;

    public void reduceStock(Map<Long, Integer> reservations) {
        List<ProductInventory> inventories = inventoryRepository.findByVariantIdIn(reservations.keySet().stream().toList());

        for (ProductInventory inventory : inventories) {
            int currentStock = inventory.getStock();
            int reduceAmount = reservations.getOrDefault(inventory.getVariantId(), 0);

            if (currentStock < reduceAmount) {
                throw new InsufficientStockException(
                        String.format(INSUFFICIENT_STOCK_MESSAGE, inventory.getVariantId())
                );
            }
        }

        inventories.forEach(inventory -> {
            int reduceAmount = reservations.getOrDefault(inventory.getVariantId(), 0);
            inventory.setStock(inventory.getStock() - reduceAmount);
        });

        inventoryRepository.saveAll(inventories);
    }


}
