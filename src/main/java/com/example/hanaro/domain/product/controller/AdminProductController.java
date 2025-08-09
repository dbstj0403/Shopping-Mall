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

import java.util.List;
import java.util.Objects;

@Validated
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "PRODUCT API", description = "어드민 전용 상품 관리 API입니다.")
public class AdminProductController {

    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;

    // === Swagger 스키마(단일 이미지) ===
    @Schema(name = "CreateProductSingleImageMultipart")
    static class CreateProductSingleImageMultipart {
        @Schema(description = "상품 정보(JSON)", implementation = ProductCreateRequestDto.class)
        public ProductCreateRequestDto data;

        @Schema(type = "string", format = "binary", description = "상품 이미지 (최대 1장)")
        public MultipartFile image;
    }

    @Operation(
            summary = "상품 등록 (이미지 0 또는 1장)",
            description = "상품 정보(JSON)와 단일 이미지를 업로드합니다. 이미지가 없으면 DB에는 null로 저장되고, 응답 변환 시 기본 이미지로 치환됩니다. (파일 ≤ 512KB)"
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
            ) ProductCreateRequestDto data,

            @RequestPart(value = "image", required = false)
            @Parameter(
                    description = "상품 이미지 (1장)",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            ) MultipartFile image
    ) {
        try {
            String imageUrl = (image != null && !image.isEmpty())
                    ? fileStorageService.saveImage(image)
                    : null;

            Product saved = productRepository.save(
                    Product.builder()
                            .name(data.getName())
                            .price(data.getPrice())
                            .description(data.getDescription())
                            .imageUrl(imageUrl) // 단일
                            .stock(Objects.requireNonNullElse(data.getStock(), 0))
                            .build()
            );

            return ResponseEntity.status(201)
                    .body(ApiResponseDto.ok("상품이 등록되었습니다.", ProductResponseDto.fromEntity(saved)));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(ErrorCode.INVALID_INPUT.getStatus())
                    .body(ApiResponseDto.fail(ErrorCode.INVALID_INPUT, e.getMessage()));
        } catch (org.springframework.web.multipart.MaxUploadSizeExceededException e) {
            return ResponseEntity.status(ErrorCode.INVALID_INPUT.getStatus())
                    .body(ApiResponseDto.fail(ErrorCode.INVALID_INPUT, "업로드 용량 제한을 초과했습니다. (≤ 512KB)"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(ErrorCode.INTERNAL_ERROR.getStatus())
                    .body(ApiResponseDto.fail(ErrorCode.INTERNAL_ERROR, "예상치 못한 오류가 발생했습니다."));
        }
    }

    @Operation(summary = "상품 목록 조회", description = "전체 상품 목록을 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> getAllProducts() {
        List<ProductResponseDto> products = productRepository.findAll().stream()
                .map(ProductResponseDto::fromEntity) // 기본 이미지 치환 보장
                .toList();
        return ResponseEntity.ok(ApiResponseDto.ok("상품 목록 조회 성공", products));
    }

    @Operation(summary = "상품 삭제", description = "상품 ID로 단일 상품을 삭제합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<Void>> deleteProduct(
            @PathVariable("id") @Positive(message = "유효한 상품 ID여야 합니다.") Long id
    ) {
        return productRepository.findById(id)
                .map(product -> {
                    try { fileStorageService.deleteImageByUrl(product.getImageUrl()); } catch (Exception ignored) {}
                    productRepository.delete(product);
                    return ResponseEntity.ok(ApiResponseDto.ok("상품이 삭제되었습니다.", (Void) null));
                })
                .orElseGet(() ->
                        ResponseEntity.status(ErrorCode.NOT_FOUND.getStatus())
                                .body(ApiResponseDto.fail(ErrorCode.NOT_FOUND, "상품을 찾을 수 없습니다."))
                );
    }

    // === 수정: 이미지 안 보내면 유지, 보내면 교체 ===
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
                - image가 있으면: 기존 이미지 삭제 후 새 이미지 저장하여 교체
                - image가 없으면: 이미지 변경 없음
                - 파일 ≤ 512KB
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
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> updateProductSingleImage(
            @PathVariable("id") @Positive(message = "유효한 상품 ID여야 합니다.") Long id,
            @Valid @RequestPart("data") ProductUpdateRequestDto req,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            Product p = productRepository.findById(id).orElse(null);
            if (p == null) {
                return ResponseEntity.status(ErrorCode.NOT_FOUND.getStatus())
                        .body(ApiResponseDto.fail(ErrorCode.NOT_FOUND, "상품을 찾을 수 없습니다."));
            }

            p.setName(req.getName());
            p.setPrice(req.getPrice());
            p.setDescription(req.getDescription());
            p.setStock(req.getStock());

            if (image != null && !image.isEmpty()) {
                String newUrl = fileStorageService.saveImage(image); // 검증 포함
                try { fileStorageService.deleteImageByUrl(p.getImageUrl()); } catch (Exception ignored) {}
                p.setImageUrl(newUrl);
            }

            Product saved = productRepository.save(p);
            return ResponseEntity.ok(ApiResponseDto.ok("상품이 수정되었습니다.", ProductResponseDto.fromEntity(saved)));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(ErrorCode.INVALID_INPUT.getStatus())
                    .body(ApiResponseDto.fail(ErrorCode.INVALID_INPUT, e.getMessage()));
        } catch (org.springframework.web.multipart.MaxUploadSizeExceededException e) {
            return ResponseEntity.status(ErrorCode.INVALID_INPUT.getStatus())
                    .body(ApiResponseDto.fail(ErrorCode.INVALID_INPUT, "업로드 용량 제한을 초과했습니다. (≤ 512KB)"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(ErrorCode.INTERNAL_ERROR.getStatus())
                    .body(ApiResponseDto.fail(ErrorCode.INTERNAL_ERROR, "예상치 못한 오류가 발생했습니다."));
        }
    }
}
