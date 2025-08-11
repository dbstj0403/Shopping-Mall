package com.example.hanaro.domain.statistics.repository;

import com.example.hanaro.domain.order.entity.OrderItem;
import com.example.hanaro.domain.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface AggOrderItemRepository extends JpaRepository<OrderItem, Long> {

    interface ProductDailyAgg {
        Long getProductId();
        Long getQty();    // sum(quantity) → Long
        Long getSales();  // sum(unitPrice*quantity) → Long
    }

    @Query("""
        select oi.product.id as productId,
               sum(oi.quantity) as qty,
               sum(oi.unitPrice * oi.quantity) as sales
        from OrderItem oi
        join oi.order o
        where o.createdAt >= :start
          and o.createdAt <  :end
        group by oi.product.id
    """)
    List<ProductDailyAgg> aggregatePerProduct(@Param("start") LocalDateTime start,
                                              @Param("end")   LocalDateTime end);
}
