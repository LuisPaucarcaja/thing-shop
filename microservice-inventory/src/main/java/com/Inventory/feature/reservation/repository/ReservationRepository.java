package com.Inventory.feature.reservation.repository;

import com.Inventory.feature.reservation.enums.ReservationStatus;
import com.Inventory.feature.reservation.entity.ProductReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ProductReservation, Long> {

    List<ProductReservation> findByOrderId(Long orderId);
    List<ProductReservation> findByVariantIdIn(List<Long> variantIds);

    List<ProductReservation> findByStatusAndExpiresAtAfterAndVariantIdIn(
            ReservationStatus status, LocalDateTime now, List<Long> variantIds);


}
