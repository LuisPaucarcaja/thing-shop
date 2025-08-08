package MicroservicePago.feature.order.service.helper;

import MicroservicePago.common.exception.EmptyCartException;
import MicroservicePago.common.exception.OutOfStockException;
import MicroservicePago.feature.order.client.CartClient;
import MicroservicePago.feature.order.client.InventoryClient;
import MicroservicePago.feature.order.dto.CartItem;
import MicroservicePago.feature.order.dto.OutOfStockItem;
import MicroservicePago.feature.order.dto.StockLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartStockValidator {

    private final InventoryClient inventoryClient;
    private final CartClient cartClient;

    public List<CartItem> checkCartStock(){
        List<CartItem> cartItems = cartClient.getCartByUser().getCartItems();

        if(cartItems.isEmpty()){
            throw new EmptyCartException();
        }

        List<OutOfStockItem> outOfStockItems = new ArrayList<>();

        List<StockLevel> stockLevels = inventoryClient.getInventoriesByVariantIds(cartItems.stream()
                .map(CartItem::getVariantId).toList());

        Map<Long, Integer> stockLevelsMap = castStockLevelToMap(stockLevels);

        cartItems.forEach(
                cartItem -> {
                    int available = stockLevelsMap.getOrDefault(cartItem.getVariantId(), 0);

                    if (available < cartItem.getQuantity()) {
                        outOfStockItems.add(
                                new OutOfStockItem(
                                        cartItem.getVariantId(),
                                        cartItem.getQuantity(),
                                        Math.max(available, 0)   // avoid -1
                                )
                        );
                    }

                }
        );


        if(!outOfStockItems.isEmpty()){
            throw new OutOfStockException(outOfStockItems);
        }

        return cartItems;
    }


    private Map<Long, Integer> castStockLevelToMap(List<StockLevel> stockLevels){
        return stockLevels.stream().collect(Collectors.toMap(
                StockLevel::getVariantId,
                StockLevel::getStock
        ));
    }



}
