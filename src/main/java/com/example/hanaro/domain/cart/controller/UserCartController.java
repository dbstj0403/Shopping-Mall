package com.example.hanaro.domain.cart.controller;

import com.example.hanaro.domain.cart.dto.AddToCartRequestDto;
import com.example.hanaro.domain.cart.dto.AddToCartRequestDto;
import com.example.hanaro.domain.cart.dto.CartResponseDto;
import com.example.hanaro.domain.cart.dto.CartResponseDto;
import com.example.hanaro.domain.cart.service.CartService;
import com.example.hanaro.global.payload.response.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/cart")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "CART API", description = "장바구니 API")
public class UserCartController {

    private final CartService cartService;

    @Operation(summary = "장바구니 담기", description = "상품을 장바구니에 담습니다. 이미 있으면 수량 증가.")
    @PostMapping(value = "/items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<CartResponseDto>> addItem(
            @AuthenticationPrincipal Long userId,  // JWT에서 userId 주입
            @Valid @RequestBody AddToCartRequestDto req
    ) {
        CartResponseDto res = cartService.addItem(userId, req);
        return ResponseEntity.ok(ApiResponseDto.ok("장바구니 담기 성공", res));
    }

    @Operation(summary = "장바구니 조회", description = "사용자의 장바구니를 조회합니다.")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<CartResponseDto>> getCart(
            @AuthenticationPrincipal Long userId
    ) {
        CartResponseDto res = cartService.getCart(userId);
        return ResponseEntity.ok(ApiResponseDto.ok("장바구니 조회 성공", res));
    }
}
