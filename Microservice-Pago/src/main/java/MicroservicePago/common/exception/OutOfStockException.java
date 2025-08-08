package MicroservicePago.common.exception;

import MicroservicePago.feature.order.dto.OutOfStockItem;
import lombok.Getter;

import java.util.List;

import static MicroservicePago.common.constants.ErrorMessageConstants.OUT_OF_STOCK_MESSAGE;

@Getter
public class OutOfStockException extends RuntimeException {
    private final List<OutOfStockItem> outOfStockItems;

    public OutOfStockException(List<OutOfStockItem> outOfStockItems) {
        super(OUT_OF_STOCK_MESSAGE);
        this.outOfStockItems = outOfStockItems;
    }

}

