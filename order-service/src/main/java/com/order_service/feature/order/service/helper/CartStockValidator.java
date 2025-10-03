package com.order_service.feature.order.service.helper;

import com.order_service.common.exception.EmptyCartException;
import com.order_service.common.exception.OutOfStockException;
import com.order_service.feature.order.client.CartClient;
import com.order_service.feature.order.client.InventoryClient;
import com.order_service.feature.order.dto.CartItem;
import com.order_service.feature.order.dto.OutOfStockItem;
import com.order_service.feature.order.dto.StockLevel;
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
