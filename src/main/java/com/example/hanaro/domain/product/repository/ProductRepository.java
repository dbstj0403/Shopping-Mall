package com.example.hanaro.domain.product.repository;

import com.example.hanaro.domain.product.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;   // <= 추가
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = "images")   // <= 이미지 함께 페치
    List<Product> findByStockGreaterThan(int stock);

    @EntityGraph(attributePaths = "images")   // <= 이미지 함께 페치
    List<Product> findByNameContainingIgnoreCaseAndStockGreaterThan(String name, int stock);

    // 상세 조회도 이미지 함께 페치
    @EntityGraph(attributePaths = "images")
    Optional<Product> findWithImagesById(Long id);

    @Modifying(clearAutomatically = false, flushAutomatically = false)
    @Query("""
        UPDATE Product p SET p.stock = p.stock - :qty
        WHERE p.id = :productId AND p.stock >= :qty
    """)
    int decreaseStockIfEnough(@Param("productId") Long productId, @Param("qty") int qty);
}
