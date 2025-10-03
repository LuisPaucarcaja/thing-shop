package com.Inventory_service.feature.reservation.service;

import com.Inventory_service.feature.reservation.entity.ProductReservation;

import java.util.List;

public interface ReservationService {

    List<ProductReservation> findReservationsActiveByVariants(List<Long> variantIds);

    void createReservations(Long orderId);

    void confirmReservations(Long orderId);

}
