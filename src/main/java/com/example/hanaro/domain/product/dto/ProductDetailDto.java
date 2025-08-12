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
@Schema(description = "사용자용 상품 상세 DTO")
public class ProductDetailDto {

    @Schema(description = "상품 ID", example = "1")
    private Long id;

    @Schema(description = "상품 이름", example = "초코 쿠키")
    private String name;

    @Schema(description = "상품 가격", example = "2500")
    private Integer price;

    @Schema(description = "상품 설명", example = "달콤달콤 수제 쿠키")
    private String description;

    @ArraySchema(arraySchema = @Schema(description = "상품 이미지 URL 배열"),
            schema = @Schema(description = "상품 이미지 URL", example = "https://cdn.example.com/products/1_0.png"))
    private List<String> imageUrls;

    public static ProductDetailDto fromEntity(Product p) {
        List<String> urls = p.getImages() == null ? List.of()
                : p.getImages().stream()
                .sorted(Comparator.comparingInt(ProductImage::getOrderNo)
                        .thenComparing(ProductImage::getId))
                .map(ProductImage::getUrl)
                .toList();

        return ProductDetailDto.builder()
                .id(p.getId())
                .name(p.getName())
                .price(p.getPrice())
                .description(p.getDescription())
                .imageUrls(urls)
                .build();
    }
}
