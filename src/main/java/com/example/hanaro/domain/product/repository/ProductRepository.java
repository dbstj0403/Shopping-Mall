package com.example.hanaro.domain.product.repository;

import com.example.hanaro.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStockGreaterThan(int stock);
    List<Product> findByNameContainingIgnoreCaseAndStockGreaterThan(String name, int stock);

    @Modifying(clearAutomatically = false, flushAutomatically = false)
    @Query("""
        UPDATE Product p SET p.stock = p.stock - :qty
        WHERE p.id = :productId AND p.stock >= :qty
    """)
    int decreaseStockIfEnough(@Param("productId") Long productId, @Param("qty") int qty);
}
