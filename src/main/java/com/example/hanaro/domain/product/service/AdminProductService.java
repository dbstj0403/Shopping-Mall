package com.example.hanaro.domain.product.service;

import com.example.hanaro.domain.product.dto.ProductCreateRequestDto;
import com.example.hanaro.domain.product.dto.ProductResponseDto;
import com.example.hanaro.domain.product.dto.ProductUpdateRequestDto;
import com.example.hanaro.domain.product.entity.Product;
import com.example.hanaro.domain.product.repository.ProductRepository;
import com.example.hanaro.global.error.CustomException;
import com.example.hanaro.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminProductService {

    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;
    private static final Logger bizProd = LoggerFactory.getLogger("business.product");

    @Transactional
    public ProductResponseDto createProduct(ProductCreateRequestDto data, MultipartFile image) throws IOException {
        String imageUrl = (image != null && !image.isEmpty())
                ? fileStorageService.saveImage(image)
                : null;

        Product saved = productRepository.save(
                Product.builder()
                        .name(data.getName())
                        .price(data.getPrice())
                        .description(data.getDescription())
                        .imageUrl(imageUrl)
                        .stock(Objects.requireNonNullElse(data.getStock(), 0))
                        .build()
        );

        bizProd.info("create id={} name='{}' price={} stock={} image={}",
                saved.getId(), saved.getName(), saved.getPrice(), saved.getStock(), saved.getImageUrl());

        return ProductResponseDto.fromEntity(saved);
    }

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "상품을 찾을 수 없습니다. id=" + id));
        try { fileStorageService.deleteImageByUrl(product.getImageUrl()); } catch (Exception ignored) {}
        productRepository.delete(product);
        bizProd.info("delete id={} name='{}'", product.getId(), product.getName());
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductUpdateRequestDto req, MultipartFile image) throws IOException {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "상품을 찾을 수 없습니다. id=" + id));

        Integer beforePrice = p.getPrice();
        Integer beforeStock = p.getStock();
        String  beforeName  = p.getName();

        p.setName(req.getName());
        p.setPrice(req.getPrice());
        p.setDescription(req.getDescription());
        p.setStock(req.getStock());

        if (image != null && !image.isEmpty()) {
            String newUrl = fileStorageService.saveImage(image); // 내부에서 용량/확장자 검증
            try { fileStorageService.deleteImageByUrl(p.getImageUrl()); } catch (Exception ignored) {}
            p.setImageUrl(newUrl);
        }

        bizProd.info("update id={} name:{}->{} price:{}->{} stock:{}->{}",
                p.getId(), beforeName, p.getName(), beforePrice, p.getPrice(), beforeStock, p.getStock());


        // 변경감지로 flush
        return ProductResponseDto.fromEntity(p);
    }

    @Transactional
    public ProductResponseDto updateStock(Long id, int newStock) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "상품을 찾을 수 없습니다. id=" + id));
        p.setStock(newStock);
        int before = p.getStock();
        bizProd.info("stock-update id={} before={} after={}", p.getId(), before, newStock);

        return ProductResponseDto.fromEntity(p);
    }
}
