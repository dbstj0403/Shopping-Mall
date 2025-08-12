package com.example.hanaro.domain.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(columnDefinition = "text")
    private String description;

    @NotNull(message = "재고는 필수입니다.")
    @PositiveOrZero(message = "재고는 0 이상이어야 합니다.")
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer stock;

    @Version
    private Long version;

    // 이미지들 (순서 보장용 orderNo, 대표 여부 isPrimary)
    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderNo ASC, id ASC")
    private List<ProductImage> images = new ArrayList<>();

    // 편의 메서드
    public void setImages(List<ProductImage> newImages) {
        this.images.clear();
        if (newImages != null) {
            newImages.forEach(this::addImage);
        }
    }

    public void addImage(ProductImage img) {
        img.setProduct(this);
        this.images.add(img);
    }
}
