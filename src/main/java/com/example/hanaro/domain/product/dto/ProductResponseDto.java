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
    private Integer price;
    private String description;
    private List<String> imageUrls;
}
