package com.example.hanaro.domain.product.service;

import com.example.hanaro.domain.product.dto.ProductCreateRequestDto;
import com.example.hanaro.domain.product.dto.ProductResponseDto;
import com.example.hanaro.domain.product.dto.ProductUpdateRequestDto;
import com.example.hanaro.domain.product.entity.Product;
import com.example.hanaro.domain.product.entity.ProductImage;
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
import java.util.ArrayList;
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
    public ProductResponseDto createProduct(ProductCreateRequestDto data, List<MultipartFile> images) throws IOException {
        Product p = Product.builder()
                .name(data.getName())
                .price(data.getPrice())
                .description(data.getDescription())
                .stock(Objects.requireNonNullElse(data.getStock(), 0))
                .build();

        // 이미지 저장
        List<String> urls = fileStorageService.saveImages(images);
        p.setImages(buildImageEntities(urls));

        Product saved = productRepository.save(p);

        bizProd.info("create id={} name='{}' price={} stock={} images={}",
                saved.getId(), saved.getName(), saved.getPrice(), saved.getStock(), saved.getImages().size());

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
        // 실제 파일 삭제
        fileStorageService.deleteByUrls(product.getImages().stream().map(ProductImage::getUrl).toList());
        productRepository.delete(product);
        bizProd.info("delete id={} name='{}'", product.getId(), product.getName());
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductUpdateRequestDto req, List<MultipartFile> newImages) throws IOException {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "상품을 찾을 수 없습니다. id=" + id));

        Integer beforePrice = p.getPrice();
        Integer beforeStock = p.getStock();
        String  beforeName  = p.getName();

        p.setName(req.getName());
        p.setPrice(req.getPrice());
        p.setDescription(req.getDescription());
        p.setStock(req.getStock());

        // 이미지 교체: 새 이미지가 1장 이상 들어오면 전량 교체(파일 삭제 후 재저장)
        boolean hasNew = newImages != null && newImages.stream().anyMatch(f -> f != null && !f.isEmpty());
        if (hasNew) {
            // 기존 파일 삭제
            fileStorageService.deleteByUrls(p.getImages().stream().map(ProductImage::getUrl).toList());
            // 새로 저장
            List<String> urls = fileStorageService.saveImages(newImages);
            p.setImages(buildImageEntities(urls));
        }

        bizProd.info("update id={} name:{}->{} price:{}->{} stock:{}->{} (images replaced: {})",
                p.getId(), beforeName, p.getName(), beforePrice, p.getPrice(), beforeStock, p.getStock(), hasNew);

        return ProductResponseDto.fromEntity(p);
    }

    @Transactional
    public ProductResponseDto updateStock(Long id, int newStock) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "상품을 찾을 수 없습니다. id=" + id));
        int before = p.getStock();
        p.setStock(newStock);
        bizProd.info("stock-update id={} before={} after={}", p.getId(), before, newStock);
        return ProductResponseDto.fromEntity(p);
    }

    // helper
    private List<ProductImage> buildImageEntities(List<String> urls) {
        List<ProductImage> list = new ArrayList<>();
        int i = 0;
        for (String url : urls) {
            list.add(ProductImage.builder()
                    .url(url)
                    .orderNo(i)
                    .isPrimary(i == 0) // 첫 번째를 대표로
                    .build());
            i++;
        }
        return list;
    }

    @Transactional
    public ProductResponseDto deleteAllImages(Long id) {
        Product p = productRepository.findWithImagesById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "상품을 찾을 수 없습니다. id=" + id));

        // 현재 이미지 URL 수집
        List<String> urls = p.getImages().stream().map(ProductImage::getUrl).toList();

        // 스토리지 파일 삭제 (이미 없더라도 idempotent 하게 처리한다고 가정)
        if (!urls.isEmpty()) {
            fileStorageService.deleteByUrls(urls);
        }

        // DB 관계 제거 (orphanRemoval=true 이므로 이미지 레코드도 삭제됨)
        p.getImages().clear();

        bizProd.info("delete-all-images id={} removedCount={}", p.getId(), urls.size());

        // 빈 이미지 리스트가 반영된 현재 상태 반환
        return ProductResponseDto.fromEntity(p);
    }
}
