package com.example.hanaro.domain.product.controller;

import com.example.hanaro.domain.product.dto.ProductResponseDto;
import com.example.hanaro.domain.product.repository.ProductRepository;
import com.example.hanaro.global.payload.response.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/products")
@RequiredArgsConstructor
@Tag(name = "PRODUCT API", description = "일반 사용자용 상품 관련 API입니다.")
public class UserProductController {
    private final ProductRepository productRepository;

    @Operation(summary = "사용자용 전체 상품 조회", description = "재고가 1개 이상인 상품 전체 목록 반환")
    @GetMapping("")
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> getAllAvailableProducts() {
        List<ProductResponseDto> products = productRepository.findByStockGreaterThan(0)
                .stream()
                .map(ProductResponseDto::fromEntity)
                .toList();

        return ResponseEntity.ok(ApiResponseDto.ok("상품 목록 조회 성공", products));
    }

    @Operation(summary = "사용자용 상품 검색", description = "상품명을 포함하는 재고 1개 이상 상품 검색")
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<?>> searchProducts(
            @RequestParam("q") String keyword
    ) {
        List<ProductResponseDto> products = productRepository
                .findByNameContainingIgnoreCaseAndStockGreaterThan(keyword, 0)
                .stream()
                .map(ProductResponseDto::fromEntity)
                .toList();

        if (products.isEmpty()) {
            return ResponseEntity.ok(ApiResponseDto.ok("검색 결과가 없습니다", List.of()));
        }

        return ResponseEntity.ok(ApiResponseDto.ok("상품 검색 성공", products));
    }
}
