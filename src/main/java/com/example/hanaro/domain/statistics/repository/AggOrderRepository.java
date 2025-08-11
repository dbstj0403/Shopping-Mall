package com.example.hanaro.domain.statistics.repository;

import com.example.hanaro.domain.order.entity.Order;
import com.example.hanaro.domain.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;

public interface AggOrderRepository extends JpaRepository<Order, Long> {

    interface DayTotals {
        Number getTotalSales();
        Number getTotalOrders();
    }

    @Query("""
        select
          coalesce(sum(oi.unitPrice * oi.quantity), 0) as totalSales,
          count(distinct o.id)                           as totalOrders
        from Order o
        join o.items oi
        where o.createdAt >= :start
          and o.createdAt <  :end
    """)
    DayTotals sumDayAndCount(@Param("start") LocalDateTime start,
                             @Param("end")   LocalDateTime end);
}
