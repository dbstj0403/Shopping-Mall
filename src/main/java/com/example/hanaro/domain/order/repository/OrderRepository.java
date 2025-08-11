package com.example.hanaro.domain.order.repository;

import com.example.hanaro.domain.order.entity.Order;
import com.example.hanaro.domain.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser_IdOrderByIdDesc(Long userId);

    @EntityGraph(attributePaths = { "items", "items.product" })
    Optional<Order> findByIdAndUser_Id(Long id, Long userId);

    @EntityGraph(attributePaths = {"user"})
    List<Order> findAllByOrderByCreatedAtDesc();

    // [A] orderId로 조회 (연관 전부 로딩)
    @Override
    @EntityGraph(attributePaths = {"user", "items", "items.product"})
    Optional<Order> findById(Long id);

    // [B] 상품명으로 조회 (연관 fetch join + DISTINCT)
    @Query("""
        select distinct o
        from Order o
        left join fetch o.user u
        left join fetch o.items i
        left join fetch i.product p
        where lower(p.name) like lower(concat('%', :name, '%'))
        order by o.createdAt desc
    """)
    List<Order> findAllWithItemsByProductName(@Param("name") String name);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
      update Order o
         set o.status = :to, o.statusChangedAt = CURRENT_TIMESTAMP
       where o.status = :from
         and o.statusChangedAt <= :cutoff
    """)
    int bulkAdvance(
            @Param("from") OrderStatus from,
            @Param("to") OrderStatus to,
            @Param("cutoff") LocalDateTime cutoff
    );
}
