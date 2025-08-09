package com.example.hanaro.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data @Builder
@Schema(description = "상품 등록 응답 DTO")
public class ProductResponseDto {
    @Schema(description = "상품 ID", example = "1")
    private Long id;

    @Schema(description = "상품 이름", example = "아이패드")
    private String name;

    @Schema(description = "상품 가격", example = "1000")
    private Integer price;

    @Schema(description = "상품 설명", example = "대학생 필수품")
    private String description;

    private List<String> imageUrls;

    @Schema(description = "현재 재고", example = "25")
    private Integer stock;
}
