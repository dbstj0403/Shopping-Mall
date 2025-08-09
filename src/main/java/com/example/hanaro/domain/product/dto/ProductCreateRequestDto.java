package com.example.hanaro.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductCreateRequestDto {
    @Schema(description = "상품 이름", example = "아이패드")
    @NotBlank(message = "상품명은 비어 있을 수 없습니다.")
    private String name;

    @Schema(description = "상품 가격", example = "1000,000원")
    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price;

    @Schema(description = "상품 설명", example = "대학생 필수품")
    @Size(max = 1000, message = "상품 설명은 1000자를 넘을 수 없습니다.")
    private String description;
}
