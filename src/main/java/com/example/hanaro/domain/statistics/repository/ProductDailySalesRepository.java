package com.example.hanaro.domain.statistics.repository;

import com.example.hanaro.domain.statistics.entity.ProductDailySales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDailySalesRepository
        extends JpaRepository<ProductDailySales, ProductDailySales.Pk> {
}
