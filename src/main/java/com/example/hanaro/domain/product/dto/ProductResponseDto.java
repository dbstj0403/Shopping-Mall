package com.example.hanaro.domain.product.dto;

import com.example.hanaro.domain.product.entity.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "상품 응답 DTO")
public class ProductResponseDto {

    @Schema(description = "상품 ID", example = "1")
    private Long id;

    @Schema(description = "상품 이름", example = "아이패드")
    private String name;

    @Schema(description = "상품 가격", example = "1000")
    private Integer price;

    @Schema(description = "상품 설명", example = "대학생 필수품")
    private String description;

    @Schema(description = "대표 이미지 URL", example = "/static/origin/default_product.png")
    private String imageUrl;

    @Schema(description = "현재 재고", example = "25")
    private Integer stock;

    private static final String DEFAULT_IMAGE = "/static/origin/default_product.png";

    public static ProductResponseDto fromEntity(Product p) {
        return ProductResponseDto.builder()
                .id(p.getId())
                .name(p.getName())
                .price(p.getPrice())
                .description(p.getDescription())
                .imageUrl(p.getImageUrl())
                .stock(p.getStock())
                .build();
    }

    // 응답 직전에 기본 이미지 보장
    @JsonProperty("imageUrl")
    public String getImageUrlForJson() {
        return (imageUrl == null || imageUrl.isBlank()) ? DEFAULT_IMAGE : imageUrl;
    }
}
