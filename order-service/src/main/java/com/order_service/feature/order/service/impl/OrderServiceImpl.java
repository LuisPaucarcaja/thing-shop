package com.order_service.feature.order.service.impl;

import com.order_service.feature.order.dto.OrderDto;
import com.order_service.feature.order.event.producer.ReservationEventProducer;
import com.order_service.feature.order.entity.Order;
import com.order_service.feature.order.entity.OrderItem;
import com.order_service.feature.order.enums.OrderStatus;
import com.order_service.feature.order.mapper.OrderMapper;
import com.order_service.feature.order.repository.OrderRepository;
import com.order_service.feature.order.dto.OrderRequest;
import com.order_service.feature.order.service.helper.CartStockValidator;
import com.order_service.feature.order.service.IOrderService;
import com.order_service.feature.order.service.helper.MercadoPagoService;
import com.order_service.feature.order.dto.CartItem;
import com.order_service.common.dto.PaginatedResponse;
import com.order_service.feature.shipping.entity.ShippingAddress;
import com.order_service.common.exception.ResourceNotFoundException;
import com.order_service.feature.shipping.service.IShippingAddressService;
import com.auth_core.security.AuthenticatedUserService;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;

import static com.order_service.common.constants.CacheConstants.GET_ORDERS_BY_USER;
import static com.order_service.common.constants.GeneralConstants.*;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final MercadoPagoService mercadoPagoService;
    private final CartStockValidator cartStockValidator;
    private final OrderMapper orderMapper;
    private final IShippingAddressService shippingAddressService;

    private final ReservationEventProducer reservationEventProducer;

    private final AuthenticatedUserService authenticatedUserService;

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(Order.class.getSimpleName(), FIELD_ID , id));

        return orderMapper.toDto(order);
    }

    @Override
    @Cacheable(GET_ORDERS_BY_USER)
    public PaginatedResponse<OrderDto> getOrdersByUser(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);
        Page<Order> orders = orderRepository.findByUserIdAndStatus(userId, OrderStatus.PAID, pageable);
        return new PaginatedResponse<>(orders.map(orderMapper::toDto));
    }

    @Transactional
    @Override
    public String createOrder(OrderRequest orderRequest) {

        Long userId = authenticatedUserService.getAuthenticatedUserId();
        List<CartItem> cartItems = cartStockValidator.checkCartStock();

        Order newOrder = saveOrder( userId, cartItems, orderRequest);


        Preference preference = mercadoPagoService.
                createPaymentPreference(newOrder.getId(), cartItems, newOrder.getShippingAddress());

        reservationEventProducer.sendReservationCreatedEvent(newOrder.getId());

        return preference.getId();
    }


    private List<OrderItem> buildOrderItems(List<CartItem> cartItems, Order order){
        return cartItems.stream()
                .map(cartItem -> OrderItem.builder()
                        .productVariantId(cartItem.getVariantId())
                        .unitPrice(cartItem.getPrice())
                        .quantity(cartItem.getQuantity())
                        .order(order)
                        .build())
                .toList();
    }

    private Order saveOrder(Long userId, List<CartItem> cartItems, OrderRequest orderRequest) {
        ShippingAddress shippingAddress = shippingAddressService.getShippingAddressById(
                                                                        orderRequest.getShippingAddressId());

        Order order = Order.builder()
                .userId(userId)
                .status(OrderStatus.PENDING_PAYMENT)
                .shippingAddress(shippingAddress)
                .build();

        order.setOrderItems(buildOrderItems(cartItems, order));

        BigDecimal totalAmount = calculateOrderTotal(cartItems, getShippingPrice(shippingAddress));
        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }

    private BigDecimal calculateOrderTotal(List<CartItem> cartItems, BigDecimal shippingPrice){
        BigDecimal itemsTotal = cartItems.stream().map(item ->
                        item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return itemsTotal.add(shippingPrice);
    }

    private BigDecimal getShippingPrice(ShippingAddress shippingAddress){
        return shippingAddress.getShippingDistrict().getShippingPrice();
    }

}
