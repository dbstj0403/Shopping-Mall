package com.example.hanaro.domain.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CartResponseDto(
        @Schema(description = "장바구니 아이템 목록") List<CartItemDto> items,
        @Schema(example = "3") Integer totalQuantity,
        @Schema(example = "7500") Integer totalAmount
) {
    public static CartResponseDto of(List<CartItemDto> items) {
        int tq = items.stream().mapToInt(CartItemDto::quantity).sum();
        int ta = items.stream().mapToInt(CartItemDto::lineTotal).sum();
        return new CartResponseDto(items, tq, ta);
    }
}
