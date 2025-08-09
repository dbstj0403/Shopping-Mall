package com.example.hanaro.domain.product.controller;

import com.example.hanaro.domain.product.dto.ProductCreateRequestDto;
import com.example.hanaro.domain.product.dto.ProductResponseDto;
import com.example.hanaro.domain.product.dto.ProductUpdateRequestDto;
import com.example.hanaro.domain.product.entity.Product;
import com.example.hanaro.domain.product.repository.ProductRepository;
import com.example.hanaro.domain.product.service.FileStorageService;
import com.example.hanaro.global.error.ErrorCode;
import com.example.hanaro.global.payload.response.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Validated
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth") // Swagger 자물쇠 표시
@Tag(name = "PRODUCT API", description = "어드민 전용 상품 관리 API입니다.")
public class AdminProductController {

    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;

    // ====== 등록 (이미지 0 또는 1장) ======
    @Schema(name = "CreateProductSingleImageMultipart")
    static class CreateProductSingleImageMultipart {
        @Schema(description = "상품 정보(JSON)", implementation = ProductCreateRequestDto.class)
        public ProductCreateRequestDto data;

        @Schema(type = "string", format = "binary", description = "상품 이미지 (최대 1장)")
        public MultipartFile image;
    }

    @Operation(
            summary = "상품 등록 (이미지 0 또는 1장)",
            description = "상품 정보(JSON)와 선택적인 단일 이미지로 상품을 등록합니다. (개별 파일 ≤ 512KB)"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = CreateProductSingleImageMultipart.class),
                    encoding = {
                            @Encoding(name = "data", contentType = MediaType.APPLICATION_JSON_VALUE),
                            @Encoding(name = "image", contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    }
            )
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> createProduct(
            @Valid
            @RequestPart("data")
            @Parameter(
                    description = "상품 정보(JSON)",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductCreateRequestDto.class),
                            examples = @ExampleObject(
                                    value = "{\"name\":\"초코 쿠키\",\"price\":2500,\"description\":\"달콤한 수제 쿠키\",\"stock\":0}"
                            )
                    )
            )
            ProductCreateRequestDto data,

            @RequestPart(value = "image", required = false)
            @Parameter(
                    description = "상품 이미지 (1장)",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            MultipartFile image
    ) {
        try {
            String imageUrl = null;
            if (image != null && !image.isEmpty()) {
                // FileStorageService가 확장자/MIME/용량 검증 수행
                imageUrl = fileStorageService.saveImage(image);
            }

            Product saved = productRepository.save(
                    Product.builder()
                            .name(data.getName())
                            .price(data.getPrice())
                            .description(data.getDescription())
                            .imageUrls(imageUrl == null ? List.of() : List.of(imageUrl)) // 0~1장 정책
                            .stock(Objects.requireNonNullElse(data.getStock(), 0))
                            .build()
            );

            ProductResponseDto resp = ProductResponseDto.builder()
                    .id(saved.getId())
                    .name(saved.getName())
                    .price(saved.getPrice())
                    .description(saved.getDescription())
                    .imageUrls(saved.getImageUrls())
                    .stock(saved.getStock())
                    .build();

            return ResponseEntity.ok(ApiResponseDto.ok("상품이 등록되었습니다.", resp));

        } catch (IllegalArgumentException e) {
            // 파일 검증 실패 등
            return ResponseEntity
                    .status(ErrorCode.INVALID_INPUT.getStatus())
                    .body(ApiResponseDto.fail(ErrorCode.INVALID_INPUT, e.getMessage()));
        } catch (org.springframework.web.multipart.MaxUploadSizeExceededException e) {
            return ResponseEntity
                    .status(ErrorCode.INVALID_INPUT.getStatus())
                    .body(ApiResponseDto.fail(ErrorCode.INVALID_INPUT, "업로드 용량 제한을 초과했습니다. (≤ 512KB)"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(ErrorCode.INTERNAL_ERROR.getStatus())
                    .body(ApiResponseDto.fail(ErrorCode.INTERNAL_ERROR, "예상치 못한 오류가 발생했습니다."));
        }
    }

    // ====== 목록 ======
    @Operation(
            summary = "상품 목록 조회",
            description = "전체 상품 목록을 조회합니다."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> getAllProducts() {
        List<ProductResponseDto> products = productRepository.findAll().stream()
                .map(p -> ProductResponseDto.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .price(p.getPrice())
                        .description(p.getDescription())
                        .imageUrls(p.getImageUrls())
                        .stock(p.getStock())
                        .build()
                )
                .toList();

        return ResponseEntity.ok(ApiResponseDto.ok("상품 목록 조회 성공", products));
    }

    // ====== 삭제 ======
    @Operation(
            summary = "상품 삭제",
            description = "상품 ID로 단일 상품을 삭제합니다."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<Void>> deleteProduct(
            @PathVariable("id") @Positive(message = "유효한 상품 ID여야 합니다.") Long id
    ) {
        return productRepository.findById(id)
                .map(product -> {
                    // 실제 파일까지 제거하려면 FileStorageService에 deleteImage(s) 구현 후 사용
                    // fileStorageService.deleteImages(product.getImageUrls());
                    productRepository.delete(product);
                    return ResponseEntity.ok(ApiResponseDto.ok("상품이 삭제되었습니다.", (Void) null));
                })
                .orElseGet(() ->
                        ResponseEntity
                                .status(ErrorCode.NOT_FOUND.getStatus())
                                .body(ApiResponseDto.fail(ErrorCode.NOT_FOUND, "상품을 찾을 수 없습니다."))
                );
    }

    // ====== 수정 (이미지 0 또는 1장) ======
    @Schema(name = "UpdateProductSingleImageMultipart")
    static class UpdateProductSingleImageMultipart {
        @Schema(description = "상품 정보(JSON)", implementation = ProductUpdateRequestDto.class)
        public ProductUpdateRequestDto data;

        @Schema(type = "string", format = "binary", description = "상품 이미지 (최대 1장)")
        public MultipartFile image;
    }

    @Operation(
            summary = "상품 전체 수정 (이미지 1장 또는 없음)",
            description = """
            상품 정보를 JSON으로 수정하고, 필요 시 단일 이미지를 업로드하여 교체할 수 있습니다.
            - image가 있고 replaceImage=true(기본): 기존 이미지 삭제 후 새 이미지로 교체
            - image가 없으면: 이미지 변경 없음
            - 개별 파일 ≤ 512KB
            """
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = UpdateProductSingleImageMultipart.class),
                    encoding = {
                            @Encoding(name = "data", contentType = MediaType.APPLICATION_JSON_VALUE),
                            @Encoding(name = "image", contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    }
            )
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> updateProductSingleImage(
            @PathVariable("id") @Positive(message = "유효한 상품 ID여야 합니다.") Long id,
            @Valid @RequestPart("data") ProductUpdateRequestDto req,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            Product p = productRepository.findById(id).orElse(null);
            if (p == null) {
                return ResponseEntity
                        .status(ErrorCode.NOT_FOUND.getStatus())
                        .body(ApiResponseDto.fail(ErrorCode.NOT_FOUND, "상품을 찾을 수 없습니다."));
            }

            // 필드 업데이트
            p.setName(req.getName());
            p.setPrice(req.getPrice());
            p.setDescription(req.getDescription());
            p.setStock(req.getStock());

            // 이미지 처리 (0~1장 정책)
            boolean hasNewImage = (image != null && !image.isEmpty());
            if (hasNewImage) {
                // FileStorageService 내부에서 확장자/MIME/용량 검증
                String newUrl = fileStorageService.saveImage(image);

                // 기존 이미지 삭제
                if (p.getImageUrls() != null) {
                    for (String url : p.getImageUrls()) {
                        try {
                            fileStorageService.deleteImage(url);
                        } catch (Exception ignored) {}
                    }
                }
                // 새 이미지로 교체
                p.setImageUrls(newUrl == null ? new ArrayList<>() : new ArrayList<>(List.of(newUrl)));
            }

            // 새 이미지 없으면 이미지 변경 없음

            Product saved = productRepository.save(p);

            ProductResponseDto dto = ProductResponseDto.builder()
                    .id(saved.getId())
                    .name(saved.getName())
                    .price(saved.getPrice())
                    .description(saved.getDescription())
                    .imageUrls(saved.getImageUrls())
                    .stock(saved.getStock())
                    .build();

            return ResponseEntity.ok(ApiResponseDto.ok("상품이 수정되었습니다.", dto));

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(ErrorCode.INVALID_INPUT.getStatus())
                    .body(ApiResponseDto.fail(ErrorCode.INVALID_INPUT, e.getMessage()));
        } catch (org.springframework.web.multipart.MaxUploadSizeExceededException e) {
            return ResponseEntity
                    .status(ErrorCode.INVALID_INPUT.getStatus())
                    .body(ApiResponseDto.fail(ErrorCode.INVALID_INPUT, "업로드 용량 제한을 초과했습니다. (≤ 512KB)"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(ErrorCode.INTERNAL_ERROR.getStatus())
                    .body(ApiResponseDto.fail(ErrorCode.INTERNAL_ERROR, "예상치 못한 오류가 발생했습니다."));
        }
    }

}
