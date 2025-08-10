package com.example.hanaro.domain.cart.repository;

import com.example.hanaro.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser_Id(Long userId);
    boolean existsByUser_Id(Long userId);
}
