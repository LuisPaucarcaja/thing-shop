package com.Inventory_service.feature.reservation.entity;


import com.Inventory_service.common.audit.Auditable;
import com.Inventory_service.feature.reservation.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
@Entity
@Table(name = "product_reservations")
public class ProductReservation extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long variantId;
    private Long orderId;
    private int reservedQuantity;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    private LocalDateTime expiresAt;
}
