package com.example.hanaro.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "product_image",
        indexes = {
                @Index(name = "idx_product_order", columnList = "product_id, orderNo")
        })
public class ProductImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(nullable = false)
    private int orderNo;     // 노출 순서 (0부터)

    @Column(nullable = false)
    private boolean isPrimary; // 대표 여부
}
