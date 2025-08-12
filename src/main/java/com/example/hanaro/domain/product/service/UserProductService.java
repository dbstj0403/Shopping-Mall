package com.example.hanaro.domain.product.service;

import com.example.hanaro.domain.product.dto.ProductDetailDto;
import com.example.hanaro.domain.product.dto.ProductListItemDto;
import com.example.hanaro.domain.product.entity.Product;
import com.example.hanaro.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProductService {

    private final ProductRepository productRepository;

    public List<ProductListItemDto> getAllAvailableProducts() {
        return productRepository.findByStockGreaterThan(0)
                .stream()
                .map(ProductListItemDto::fromEntity)
                .toList();
    }

    public List<ProductListItemDto> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCaseAndStockGreaterThan(keyword, 0)
                .stream()
                .map(ProductListItemDto::fromEntity)
                .toList();
    }

    public Optional<ProductDetailDto> getProductDetail(Long id) {
        return productRepository.findWithImagesById(id)   // <= 여기만 변경
                .map(ProductDetailDto::fromEntity);
    }

}
