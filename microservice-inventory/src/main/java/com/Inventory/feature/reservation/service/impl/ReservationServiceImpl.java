package com.Inventory.feature.reservation.service.impl;

import com.Inventory.common.client.OrderClient;
import com.Inventory.feature.inventory.dto.OrderItemDTO;
import com.Inventory.feature.reservation.enums.ReservationStatus;
import com.Inventory.feature.reservation.entity.ProductReservation;
import com.Inventory.feature.reservation.repository.ReservationRepository;
import com.Inventory.feature.inventory.service.helper.StockService;
import com.Inventory.feature.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final StockService stockService;
    private final OrderClient orderClient;

    @Value("${m2m.apikey}")
    private String m2mApiKey;
    @Override
    public List<ProductReservation> findReservationsActiveByVariants(List<Long> variantIds){
        return reservationRepository.findByStatusAndExpiresAtAfterAndVariantIdIn(ReservationStatus.RESERVED,
                LocalDateTime.now(), variantIds);
    }

    @Override
    public void createReservations(Long orderId){
        List<OrderItemDTO> orderItems = orderClient.getOrderById(orderId, m2mApiKey);
        List<ProductReservation> reservations = orderItems.stream()
                        .map( orderItem -> ProductReservation.builder()
                                .variantId(orderItem.getProductVariantId())
                                .reservedQuantity(orderItem.getQuantity())
                                .orderId(orderId)
                                .status(ReservationStatus.RESERVED)
                                .expiresAt(LocalDateTime.now().plusMinutes(20))
                                .build()).collect(Collectors.toList());

            reservationRepository.saveAll(reservations);
    }

    @Transactional
    public void confirmReservations(Long orderId) {
        List<ProductReservation> reservations = getReservationsByOrder(orderId);
        stockService.reduceStock(extractQuantitiesById(reservations));

        List<ProductReservation> modifiedReservations = reservations.stream()
                .peek(r -> {
                    if (r.getStatus().equals(ReservationStatus.RESERVED)) {
                        r.setStatus(ReservationStatus.CONFIRMED);
                    }
                }).toList();

        reservationRepository.saveAll(modifiedReservations);
    }



    private List<ProductReservation> getReservationsByOrder(Long orderId){
        return reservationRepository.findByOrderId(orderId);
    }

    private Map<Long, Integer> extractQuantitiesById(List<ProductReservation> reservations){
        return reservations.stream().collect(Collectors.toMap(ProductReservation::getVariantId,
                ProductReservation::getReservedQuantity));
    }

}
