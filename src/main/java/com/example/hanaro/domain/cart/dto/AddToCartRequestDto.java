package com.example.hanaro.domain.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddToCartRequestDto(
        @Schema(description = "상품 ID", example = "1")
        @NotNull Long productId,

        @Schema(description = "수량(양수)", example = "2")
        @NotNull @Positive Integer quantity
) {}
