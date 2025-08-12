package com.example.hanaro.domain.product.dto;

import com.example.hanaro.domain.product.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data @Builder
@Schema(description = "사용자용 상품 목록 아이템 DTO")
public class ProductListItemDto {

    @Schema(description = "상품 ID", example = "1")
    private Long id;

    @Schema(description = "상품 이름", example = "초코 쿠키")
    private String name;

    @Schema(description = "상품 가격", example = "2500")
    private Integer price;

    public static ProductListItemDto fromEntity(Product p) {
        return ProductListItemDto.builder()
                .id(p.getId())
                .name(p.getName())
                .price(p.getPrice())
                .build();
    }
}
