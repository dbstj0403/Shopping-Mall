package com.example.hanaro.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductUpdateRequestDto {

    @Schema(description = "상품 이름", example = "아이패드 에어")
    @NotBlank(message = "상품명은 비어 있을 수 없습니다.")
    private String name;

    @Schema(description = "상품 가격(원)", example = "890000")
    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 1, message = "가격은 1원 이상이어야 합니다.")
    private Integer price;

    @Schema(description = "상품 설명", example = "M2 칩셋 탑재 모델")
    @Size(max = 1000, message = "상품 설명은 1000자를 넘을 수 없습니다.")
    private String description;

    @Schema(description = "현재 재고", example = "25")
    @NotNull(message = "재고는 필수입니다.")
    @PositiveOrZero(message = "재고는 0 이상이어야 합니다.")
    private Integer stock;
}
