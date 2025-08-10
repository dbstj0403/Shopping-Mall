package com.example.hanaro.domain.product.dto;

import com.example.hanaro.domain.product.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data @Builder
@Schema(description = "사용자용 상품 상세 DTO")
public class ProductDetailDto {
    @Schema(description = "상품 이름", example = "초코 쿠키")
    private String name;

    @Schema(description = "상품 가격", example = "2500")
    private Integer price;

    @Schema(description = "상품 설명", example = "달콤달콤 수제 쿠키")
    private String description;

    public static ProductDetailDto fromEntity(Product p) {
        return ProductDetailDto.builder()
                .name(p.getName())
                .price(p.getPrice())
                .description(p.getDescription())
                .build();
    }
}
