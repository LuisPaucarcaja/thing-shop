package com.order_service.feature.shipping.entity;

import com.order_service.common.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "shipping_addresses")
@Getter
@Setter
public class ShippingAddress extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(length = 100)
    private String addressLine1;

    @Column(length = 80)
    private String addressLine2;

    @Column(length = 10)
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "shipping_district_id", nullable = false)
    private ShippingDistrict shippingDistrict;


    private Double latitude;
    private Double longitude;
}