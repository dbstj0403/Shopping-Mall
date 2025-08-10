package com.example.hanaro.domain.cart.controller;

import com.example.hanaro.domain.cart.dto.*;
import com.example.hanaro.domain.cart.service.CartService;
import com.example.hanaro.global.payload.response.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PostMapping(value = "/items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<CartResponseDto>> addItem(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody AddToCartRequestDto req
    ) {
        return ResponseEntity.ok(ApiResponseDto.ok("장바구니 담기 성공", cartService.addItem(userId, req)));
    }

    @Operation(summary = "장바구니 조회", description = "사용자의 장바구니를 조회합니다.")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<CartResponseDto>> getCart(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(ApiResponseDto.ok("장바구니 조회 성공", cartService.getCart(userId)));
    }

    @Operation(summary = "장바구니 수량 수정", description = "특정 상품의 수량을 값으로 **덮어쓰기**합니다. 0이면 해당 상품을 삭제합니다.")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PatchMapping(value = "/items/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<CartResponseDto>> updateItem(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequestDto req
    ) {
        return ResponseEntity.ok(ApiResponseDto.ok("장바구니 수정 성공",
                cartService.updateItemQuantity(userId, productId, req)));
    }

    @Operation(summary = "장바구니 아이템 삭제", description = "특정 상품을 장바구니에서 제거합니다.")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @DeleteMapping(value = "/items/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<CartResponseDto>> removeItem(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(ApiResponseDto.ok("장바구니 아이템 삭제 성공",
                cartService.removeItem(userId, productId)));
    }

    @Operation(summary = "장바구니 비우기", description = "모든 상품을 장바구니에서 제거합니다.")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<CartResponseDto>> clear(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(ApiResponseDto.ok("장바구니 비우기 성공", cartService.clearCart(userId)));
    }
}
