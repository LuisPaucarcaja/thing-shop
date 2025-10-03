package com.order_service.common.exception;

import com.order_service.feature.order.dto.OutOfStockItem;
import lombok.Getter;

import java.util.List;

import static com.order_service.common.constants.ErrorMessageConstants.OUT_OF_STOCK_MESSAGE;

@Getter
public class OutOfStockException extends RuntimeException {
    private final List<OutOfStockItem> outOfStockItems;

    public OutOfStockException(List<OutOfStockItem> outOfStockItems) {
        super(OUT_OF_STOCK_MESSAGE);
        this.outOfStockItems = outOfStockItems;
    }

}

