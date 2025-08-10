package com.example.hanaro.domain.cart.repository;

import com.example.hanaro.domain.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCart_IdAndProduct_Id(Long cartId, Long productId);
    List<CartItem> findAllByCart_Id(Long cartId);
}
