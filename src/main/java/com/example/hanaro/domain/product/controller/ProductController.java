package com.example.hanaro.domain.product.controller;

import com.example.hanaro.domain.product.dto.ProductCreateRequestDto;
import com.example.hanaro.domain.product.dto.ProductResponseDto;
import com.example.hanaro.domain.product.entity.Product;
import com.example.hanaro.domain.product.repository.ProductRepository;
import com.example.hanaro.domain.product.service.FileStorageService;
import com.example.hanaro.global.error.ErrorCode;
import com.example.hanaro.global.payload.response.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

import java.util.Arrays;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth") // Swagger 자물쇠 표시
@Tag(name = "PRODUCT API", description = "어드민 전용 상품 관리 API입니다.")
public class ProductController {

    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;

    @Schema(name = "CreateProductMultipart")
    static class CreateProductMultipart {
        @Schema(description = "상품 정보(JSON)", implementation = ProductCreateRequestDto.class)
        public ProductCreateRequestDto data;

        @ArraySchema(
                schema = @Schema(type = "string", format = "binary"),
                arraySchema = @Schema(description = "이미지 파일들 (여러 개 가능)")
        )
        public MultipartFile[] images;
    }

    @Operation(
            summary = "상품 등록",
            description = "상품 정보(JSON)과 이미지 파일들을 업로드합니다. (개별 ≤ 512KB, 총 ≤ 3MB)"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = CreateProductMultipart.class),
                    encoding = {
                            @Encoding(name = "data", contentType = MediaType.APPLICATION_JSON_VALUE),
                            @Encoding(name = "images", contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
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
                                    // 숫자는 따옴표 없이 (Swagger 파싱 오류 방지)
                                    value = "{\"name\":\"초코 쿠키\",\"price\":2500,\"description\":\"달콤한 수제 쿠키\"}"
                            )
                    )
            )
            ProductCreateRequestDto data,

            @RequestPart(value = "images", required = false)
            @Parameter(
                    description = "이미지 파일들 (여러 개 가능)",
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            array = @ArraySchema(schema = @Schema(type = "string", format = "binary"))
                    )
            )
            MultipartFile[] images
    ) {
        try {
            // 파일 저장
            List<String> imageUrls = fileStorageService.saveImages(
                    images == null ? List.of() : Arrays.asList(images)
            );

            // DB 저장
            Product saved = productRepository.save(
                    Product.builder()
                            .name(data.getName())
                            .price(data.getPrice())
                            .description(data.getDescription())
                            .imageUrls(imageUrls)
                            .build()
            );

            ProductResponseDto resp = ProductResponseDto.builder()
                    .id(saved.getId())
                    .name(saved.getName())
                    .price(saved.getPrice())
                    .description(saved.getDescription())
                    .imageUrls(saved.getImageUrls())
                    .build();

            return ResponseEntity.ok(ApiResponseDto.ok("상품이 등록되었습니다.", resp));

        } catch (org.springframework.web.multipart.MaxUploadSizeExceededException e) {
            // 업로드 용량 초과
            return ResponseEntity
                    .status(ErrorCode.INVALID_INPUT.getStatus())
                    .body(ApiResponseDto.fail(ErrorCode.INVALID_INPUT, "업로드 용량 제한을 초과했습니다. (파일 512KB, 총 3MB)"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(ErrorCode.INTERNAL_ERROR.getStatus())
                    .body(ApiResponseDto.fail(ErrorCode.INTERNAL_ERROR, "예상치 못한 오류가 발생했습니다."));
        }
    }

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
                        .build()
                )
                .toList();

        return ResponseEntity.ok(ApiResponseDto.ok("상품 목록 조회 성공", products));
    }

    @Operation(
            summary = "상품 삭제",
            description = "상품 ID로 단일 상품을 삭제합니다."
    )
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<Void>> deleteProduct(
            @PathVariable("id") @Positive(message = "유효한 상품 ID여야 합니다.") Long id
    ) {
        return productRepository.findById(id)
                .map(product -> {
                    // fileStorageService.deleteImages(product.getImageUrls()); // 필요 시
                    productRepository.delete(product);
                    return ResponseEntity.ok(ApiResponseDto.ok("상품이 삭제되었습니다.", (Void) null));
                })
                .orElseGet(() ->
                        ResponseEntity
                                .status(ErrorCode.NOT_FOUND.getStatus())
                                .body(ApiResponseDto.fail(ErrorCode.NOT_FOUND, "상품을 찾을 수 없습니다."))
                );
    }
}
