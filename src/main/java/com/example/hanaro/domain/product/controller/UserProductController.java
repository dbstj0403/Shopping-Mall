package com.example.hanaro.domain.product.controller;

import com.example.hanaro.domain.product.dto.ProductDetailDto;
import com.example.hanaro.domain.product.dto.ProductListItemDto;
import com.example.hanaro.domain.product.repository.ProductRepository;
import com.example.hanaro.global.error.ErrorCode;
import com.example.hanaro.global.payload.response.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/products")
@RequiredArgsConstructor
@Tag(name = "PRODUCT API", description = "일반 사용자용 상품 관련 API입니다.")
public class UserProductController {
    private final ProductRepository productRepository;

    @Operation(summary = "사용자용 전체 상품 조회", description = "재고가 1개 이상인 상품의 이름, 가격만 반환")
    @GetMapping("")
    public ResponseEntity<ApiResponseDto<List<ProductListItemDto>>> getAllAvailableProducts() {
        List<ProductListItemDto> products = productRepository.findByStockGreaterThan(0)
                .stream()
                .map(ProductListItemDto::fromEntity)
                .toList();

        return ResponseEntity.ok(ApiResponseDto.ok("상품 목록 조회 성공", products));
    }

    @Operation(summary = "사용자용 상품 검색", description = "상품명을 포함하는 재고 1개 이상 상품의 이름, 가격만 반환")
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<List<ProductListItemDto>>> searchProducts(
            @RequestParam("q") String keyword
    ) {
        List<ProductListItemDto> products = productRepository
                .findByNameContainingIgnoreCaseAndStockGreaterThan(keyword, 0)
                .stream()
                .map(ProductListItemDto::fromEntity)
                .toList();

        return ResponseEntity.ok(
                ApiResponseDto.ok(products.isEmpty() ? "검색 결과가 없습니다" : "상품 검색 성공", products)
        );
    }

    @Operation(summary = "상품 상세 조회", description = "아이디로 상품 상세(이름, 가격, 설명) 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<ProductDetailDto>> getProductDetail(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(p -> ResponseEntity.ok(
                        ApiResponseDto.ok("상품 상세 조회 성공", ProductDetailDto.fromEntity(p))
                ))
                .orElseGet(() ->
                        ResponseEntity.status(ErrorCode.NOT_FOUND.getStatus())
                                .body(ApiResponseDto.<ProductDetailDto>fail(
                                        ErrorCode.NOT_FOUND, "상품을 찾을 수 없습니다.")
                                )
                );
    }

}
