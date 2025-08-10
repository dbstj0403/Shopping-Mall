package com.example.hanaro.domain.order.entity;

import com.example.hanaro.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 주문 시점 스냅샷
    @Column(nullable = false) private String productName;
    @Column(nullable = false) private int unitPrice;
    @Column(nullable = false) private int quantity;
    @Column(nullable = false) private int lineTotal;
}
