package com.example.hanaro.domain.cart.dto;

import com.example.hanaro.domain.cart.entity.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record CartItemDto(
        @Schema(example = "1") Long productId,
        @Schema(example = "초코 쿠키") String name,
        @Schema(example = "2500") Integer price,
        @Schema(example = "2") Integer quantity,
        @Schema(example = "5000") Integer lineTotal
) {
    public static CartItemDto from(CartItem ci) {
        int price = ci.getProduct().getPrice();
        int qty = ci.getQuantity();
        return new CartItemDto(
                ci.getProduct().getId(),
                ci.getProduct().getName(),
                price,
                qty,
                price * qty
        );
    }
}
