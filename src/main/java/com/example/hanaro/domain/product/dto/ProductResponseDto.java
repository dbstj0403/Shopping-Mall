package com.example.hanaro.domain.product.dto;

import com.example.hanaro.domain.product.entity.Product;
import com.example.hanaro.domain.product.entity.ProductImage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record ProductResponseDto(
        Long id,
        String name,
        Integer price,
        String description,
        Integer stock,
        @Schema(description = "이미지 URL 목록(대표가 맨 앞)")
        List<String> images
) {
    public static ProductResponseDto fromEntity(Product p) {
        List<String> urls = p.getImages().stream()
                .sorted((a,b) -> {
                    if (a.isPrimary() == b.isPrimary()) return Integer.compare(a.getOrderNo(), b.getOrderNo());
                    return a.isPrimary() ? -1 : 1; // 대표 먼저
                })
                .map(ProductImage::getUrl)
                .toList();
        return ProductResponseDto.builder()
                .id(p.getId())
                .name(p.getName())
                .price(p.getPrice())
                .description(p.getDescription())
                .stock(p.getStock())
                .images(urls)
                .build();
    }
}
