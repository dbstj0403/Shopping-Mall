package com.example.hanaro.domain.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateCartItemRequestDto(
        @Schema(description = "변경할 수량(0이면 삭제)", example = "3")
        @NotNull @PositiveOrZero Integer quantity
) {}
