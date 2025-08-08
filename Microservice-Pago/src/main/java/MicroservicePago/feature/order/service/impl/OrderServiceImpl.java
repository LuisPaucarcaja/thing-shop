package MicroservicePago.feature.order.service.impl;

import MicroservicePago.feature.order.dto.OrderDto;
import MicroservicePago.feature.order.event.producer.ReservationEventProducer;
import MicroservicePago.feature.order.entity.Order;
import MicroservicePago.feature.order.entity.OrderItem;
import MicroservicePago.feature.order.enums.OrderStatus;
import MicroservicePago.feature.order.mapper.OrderMapper;
import MicroservicePago.feature.order.repository.OrderRepository;
import MicroservicePago.feature.order.dto.OrderRequest;
import MicroservicePago.feature.order.service.helper.CartStockValidator;
import MicroservicePago.feature.order.service.IOrderService;
import MicroservicePago.feature.order.service.helper.MercadoPagoService;
import MicroservicePago.feature.order.dto.CartItem;
import MicroservicePago.common.dto.PaginatedResponse;
import MicroservicePago.feature.shipping.entity.ShippingAddress;
import MicroservicePago.common.exception.ResourceNotFoundException;
import MicroservicePago.feature.shipping.service.IShippingAddressService;
import com.authcore.security.AuthenticatedUserService;
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

import static MicroservicePago.common.constants.CacheConstants.GET_ORDERS_BY_USER;
import static MicroservicePago.common.constants.GeneralConstants.*;

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
