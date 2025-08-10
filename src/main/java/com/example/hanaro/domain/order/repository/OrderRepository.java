package com.example.hanaro.domain.order.repository;

import com.example.hanaro.domain.order.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser_IdOrderByIdDesc(Long userId);

    @EntityGraph(attributePaths = { "items", "items.product" })
    Optional<Order> findByIdAndUser_Id(Long id, Long userId);
}
