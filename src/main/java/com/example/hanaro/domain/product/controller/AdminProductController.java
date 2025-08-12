package com.example.hanaro.domain.product.controller;

import com.example.hanaro.domain.product.dto.*;
import com.example.hanaro.domain.product.service.AdminProductService;
import com.example.hanaro.global.payload.response.ApiResponseDto;
import com.example.hanaro.global.swagger.annotations.product.*;
import com.example.hanaro.global.swagger.annotations.statistics.AdminStatsDailyListApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

import java.io.IOException;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "PRODUCT API", description = "어드민 전용 상품 관리 API입니다.")
public class AdminProductController {

    private final AdminProductService adminProductService;

    // ==== Swagger용 멀티파트 스키마 ====
    @Schema(name = "CreateProductMultiImageMultipart")
    static class CreateProductMultiImageMultipart {
        @Schema(description = "상품 정보(JSON)", implementation = ProductCreateRequestDto.class)
        public ProductCreateRequestDto data;
        @ArraySchema(schema = @Schema(type = "string", format = "binary"))
        @Schema(description = "상품 이미지들 (여러 장 가능, 파일당 ≤512KB, 총합 ≤3MB)")
        public MultipartFile[] images;
    }

    @Operation(
            summary = "상품 등록 (이미지 여러 장)",
            description = """
                - data: 상품 정보(JSON)
                - images: 0~N장 (파일당 ≤512KB, 총합 ≤3MB)
                - 저장 경로: /static/upload/yyyy/MM/dd/uuid.ext
                - 첫 번째 이미지를 대표로 지정
                """
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = CreateProductMultiImageMultipart.class),
                    encoding = {
                            @Encoding(name = "data", contentType = MediaType.APPLICATION_JSON_VALUE),
                            @Encoding(name = "images", contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    }
            )
    )
    @AdminProductCreateApiResponses
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> createProduct(
            @Valid @RequestPart("data") ProductCreateRequestDto data,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) throws IOException {
        ProductResponseDto saved = adminProductService.createProduct(data,
                images == null ? List.of() : List.of(images));
        return ResponseEntity.status(201).body(ApiResponseDto.ok("상품이 등록되었습니다.", saved));
    }

    @Operation(summary = "상품 목록 조회", description = "전체 상품 목록을 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @AdminProductListApiResponses
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> getAllProducts() {
        List<ProductResponseDto> products = adminProductService.getAllProducts();
        return ResponseEntity.ok(ApiResponseDto.ok("상품 목록 조회 성공", products));
    }

    @Operation(summary = "상품 삭제", description = "아이디로 단일 상품을 삭제합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @AdminProductDeleteApiResponses
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<Void>> deleteProduct(
            @PathVariable("id") @Positive(message = "유효한 상품 ID여야 합니다.") Long id
    ) {
        adminProductService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponseDto.ok("상품이 삭제되었습니다.", (Void) null));
    }

    // ==== 수정: 새 images가 오면 전량 교체, 없으면 기존 유지 ====
    @Schema(name = "UpdateProductMultiImageMultipart")
    static class UpdateProductMultiImageMultipart {
        @Schema(description = "상품 정보(JSON)", implementation = ProductUpdateRequestDto.class)
        public ProductUpdateRequestDto data;
        @ArraySchema(schema = @Schema(type = "string", format = "binary"))
        @Schema(description = "상품 이미지들 (여러 장 가능, 보내지 않으면 기존 유지)")
        public MultipartFile[] images;
    }

    @Operation(
            summary = "상품 전체 수정 (이미지 여러 장 교체 가능)",
            description = """
                - images가 포함되면: 기존 이미지 파일 삭제 후 새 이미지로 전량 교체
                - images가 없으면: 이미지 변경 없음
                - 파일당 ≤512KB, 총합 ≤3MB
                """
    )
    @AdminProductUpdateApiResponses
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = UpdateProductMultiImageMultipart.class),
                    encoding = {
                            @Encoding(name = "data", contentType = MediaType.APPLICATION_JSON_VALUE),
                            @Encoding(name = "images", contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    }
            )
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> updateProduct(
            @PathVariable("id") @Positive(message = "유효한 상품 ID여야 합니다.") Long id,
            @Valid @RequestPart("data") ProductUpdateRequestDto req,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) throws IOException {
        ProductResponseDto updated = adminProductService.updateProduct(id, req,
                images == null ? List.of() : List.of(images));
        return ResponseEntity.ok(ApiResponseDto.ok("상품이 수정되었습니다.", updated));
    }

    // ====== 재고만 수정 (PATCH) ======
    @Operation(summary = "상품 재고 수정 (관리자)")
    @AdminProductUpdateStockApiResponses
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/{id}/stock", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> updateStock(
            @PathVariable("id") @Positive Long id,
            @Valid @org.springframework.web.bind.annotation.RequestBody ProductStockUpdateRequestDto req
    ) {
        ProductResponseDto updated = adminProductService.updateStock(id, req.stock());
        return ResponseEntity.ok(ApiResponseDto.ok("재고가 수정되었습니다.", updated));
    }

    @Operation(
            summary = "상품 이미지 전체 삭제",
            description = "해당 상품의 모든 이미지를 제거합니다."
    )
    @AdminStatsDailyListApiResponses
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}/images", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> deleteAllImages(
            @PathVariable("id") @Positive(message = "유효한 상품 ID여야 합니다.") Long id
    ) {
        ProductResponseDto updated = adminProductService.deleteAllImages(id);
        return ResponseEntity.ok(ApiResponseDto.ok("상품 이미지가 모두 삭제되었습니다.", updated));
    }
}
