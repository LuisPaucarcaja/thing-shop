package com.Inventory.feature.inventory.service.impl;

import com.Inventory.feature.inventory.dto.StockLevel;
import com.Inventory.feature.inventory.mapper.InventoryMapper;
import com.Inventory.feature.reservation.entity.ProductReservation;
import com.Inventory.feature.inventory.repository.InventoryRepository;
import com.Inventory.feature.inventory.service.InventoryService;
import com.Inventory.feature.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {


    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final ReservationService reservationService;

    @Override
    public List<StockLevel> getReducedInventories(List<Long> variantIds) {

        Map<Long, Integer> reservations = castReservationsToMap(
                reservationService.findReservationsActiveByVariants(variantIds));

        List<StockLevel> inventories = getInventoriesByVariantIds(variantIds);

        Map<Long, StockLevel> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(StockLevel::getVariantId, Function.identity()));

        for (Long variantId : variantIds) {

            StockLevel stockLevel = inventoryMap.get(variantId);

            if (stockLevel == null) {
                stockLevel = new StockLevel();
                stockLevel.setVariantId(variantId);
                stockLevel.setStock(0);
                inventoryMap.put(variantId, stockLevel);
            }

            Integer reserved = reservations.get(variantId);
            if (reserved != null) {
                stockLevel.setStock(stockLevel.getStock() - reserved);
            }
        }

        return inventories;
    }

    private List<StockLevel> getInventoriesByVariantIds(List<Long> variantIds) {
        return inventoryRepository.findByVariantIdIn(variantIds).stream()
                .map(inventoryMapper::toDto)
                .collect(Collectors.toList());
    }


    private Map<Long, Integer> castReservationsToMap(List<ProductReservation> reservations){
        return reservations.stream().collect(Collectors.toMap(
                ProductReservation::getVariantId,
                ProductReservation::getReservedQuantity,
                Integer::sum
        ));
    }



}
