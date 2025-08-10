package com.example.hanaro.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductStockUpdateRequestDto(
        @Schema(description = "수정할 재고 수량", example = "15")
        @NotNull
        @PositiveOrZero
        Integer stock
) {}
