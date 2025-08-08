package com.Inventory.feature.inventory.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductOutOfStockDTO {

    private Long id;
    private int requestedQuantity;
    private int availableStock;


}
