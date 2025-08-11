package com.example.hanaro.domain.statistics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_sales_summary")
@Getter
@NoArgsConstructor
public class DailySalesSummary {

    @Id
    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate;

    @Column(name = "total_sales", nullable = false)
    private long totalSales;

    @Column(name = "total_orders", nullable = false)
    private int totalOrders;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public DailySalesSummary(LocalDate statDate, long totalSales, int totalOrders) {
        this.statDate = statDate;
        this.totalSales = totalSales;
        this.totalOrders = totalOrders;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
