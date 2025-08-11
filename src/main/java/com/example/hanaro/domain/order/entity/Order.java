package com.example.hanaro.domain.order.entity;

import com.example.hanaro.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false)
    private int totalQuantity;

    @Column(nullable = false)
    private int totalAmount;

    public void addItem(OrderItem item) {
        this.items.add(item);
        item.setOrder(this);
    }

    // 상태 변경 기준 시각 (전환 타이머는 이 값을 기준으로 계산)
    @Column(nullable = false)
    private LocalDateTime statusChangedAt;

    @PrePersist
    public void onCreate() {
        if (statusChangedAt == null) statusChangedAt = LocalDateTime.now();
    }

    /** 상태 변경 시 항상 이 메서드 사용 */
    public void changeStatus(OrderStatus next) {
        this.status = next;
        this.statusChangedAt = LocalDateTime.now();
    }
}
