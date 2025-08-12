package com.example.hanaro.domain.product.dto;

import com.example.hanaro.domain.product.entity.Product;
import com.example.hanaro.domain.product.entity.ProductImage;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Comparator;
import java.util.List;

@Data @Builder
@Schema(description = "사용자용 상품 목록 아이템 DTO")
public class ProductListItemDto {

    @Schema(description = "상품 ID", example = "1")
    private Long id;

    @Schema(description = "상품 이름", example = "초코 쿠키")
    private String name;

    @Schema(description = "상품 가격", example = "2500")
    private Integer price;

    @ArraySchema(arraySchema = @Schema(description = "상품 이미지 URL 배열"),
            schema = @Schema(description = "상품 이미지 URL", example = "https://cdn.example.com/products/1_0.png"))
    private List<String> imageUrls;

    public static ProductListItemDto fromEntity(Product p) {
        List<String> urls = p.getImages() == null ? List.of()
                : p.getImages().stream()
                .sorted(Comparator.comparingInt(ProductImage::getOrderNo)
                        .thenComparing(ProductImage::getId))
                .map(ProductImage::getUrl)
                .toList();

        return ProductListItemDto.builder()
                .id(p.getId())
                .name(p.getName())
                .price(p.getPrice())
                .imageUrls(urls)
                .build();
    }
}
