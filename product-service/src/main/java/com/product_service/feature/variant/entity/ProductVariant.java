package com.product_service.feature.variant.entity;

import com.product_service.common.audit.Auditable;
import com.product_service.feature.media.entity.VisualVariant;
import com.product_service.feature.product.entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_variants", indexes = {
        @Index(name = "idx_product_id", columnList = "product_id"),
        @Index(name = "idx_visual_variant_id", columnList = "visual_variant_id")
})
public class ProductVariant extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    private BigDecimal price;

    @Column(length = 100)
    private String sku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visual_variant_id")
    private VisualVariant visualVariant;


    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProductVariantAttribute> variantAttributes = new HashSet<>();


}
