package com.example.hanaro.domain.product.controller;

import com.example.hanaro.domain.product.dto.ProductDetailDto;
import com.example.hanaro.domain.product.dto.ProductListItemDto;
import com.example.hanaro.domain.product.service.UserProductService;
import com.example.hanaro.global.error.ErrorCode;
import com.example.hanaro.global.payload.response.ApiResponseDto;
import com.example.hanaro.global.swagger.annotations.product.ProductDetailApiResponses;
import com.example.hanaro.global.swagger.annotations.product.ProductListApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/products")
@RequiredArgsConstructor
@Tag(name = "PRODUCT API", description = "유저용 상품 관련 API입니다.")
public class UserProductController {

    private final UserProductService userProductService;

    @Operation(summary = "유저용 전체 상품 조회", description = "재고가 1개 이상인 상품의 이름, 가격을 반환합니다.")
    @ProductListApiResponses
    @GetMapping("")
    public ResponseEntity<ApiResponseDto<List<ProductListItemDto>>> getAllAvailableProducts() {
        List<ProductListItemDto> products = userProductService.getAllAvailableProducts();
        return ResponseEntity.ok(ApiResponseDto.ok("상품 목록 조회 성공", products));
    }

    @Operation(summary = "유저용 상품 검색", description = "상품명을 포함하는 재고 1개 이상 상품의 이름, 가격을 반환합니다.")
    @ProductListApiResponses
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<List<ProductListItemDto>>> searchProducts(
            @RequestParam("q") String keyword
    ) {
        List<ProductListItemDto> products = userProductService.searchProducts(keyword);
        return ResponseEntity.ok(
                ApiResponseDto.ok(products.isEmpty() ? "검색 결과가 없습니다" : "상품 검색 성공", products)
        );
    }

    @Operation(summary = "유저용 상품 상세 조회", description = "아이디로 상품 상세(이름, 가격, 설명)를 조회합니다.")
    @ProductDetailApiResponses
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<ProductDetailDto>> getProductDetail(@PathVariable Long id) {
        return userProductService.getProductDetail(id)
                .map(dto -> ResponseEntity.ok(ApiResponseDto.ok("상품 상세 조회 성공", dto)))
                .orElseGet(() ->
                        ResponseEntity.status(ErrorCode.NOT_FOUND.getStatus())
                                .body(ApiResponseDto.fail(ErrorCode.NOT_FOUND, "상품을 찾을 수 없습니다."))
                );
    }
}
