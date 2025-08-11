package com.example.hanaro.domain.statistics.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "product_daily_sales")
@Getter
@NoArgsConstructor
public class ProductDailySales {

    @EmbeddedId
    private Pk id;

    @Column(name = "total_qty", nullable = false)
    private int totalQty;

    @Column(name = "total_sales", nullable = false)
    private long totalSales;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public ProductDailySales(LocalDate statDate, Long productId, int totalQty, long totalSales) {
        this.id = new Pk(statDate, productId);
        this.totalQty = totalQty;
        this.totalSales = totalSales;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Pk implements Serializable {
        @Column(name = "stat_date", nullable = false)
        private LocalDate statDate;

        @Column(name = "product_id", nullable = false)
        private Long productId;

        public Pk(LocalDate statDate, Long productId) {
            this.statDate = statDate;
            this.productId = productId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pk pk)) return false;
            return Objects.equals(statDate, pk.statDate) &&
                    Objects.equals(productId, pk.productId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(statDate, productId);
        }
    }
}
