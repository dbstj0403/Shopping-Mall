package com.example.hanaro.domain.product.controller;

import com.example.hanaro.domain.product.dto.ProductCreateRequestDto;
import com.example.hanaro.domain.product.dto.ProductResponseDto;
import com.example.hanaro.domain.product.dto.ProductStockUpdateRequestDto;
import com.example.hanaro.domain.product.dto.ProductUpdateRequestDto;
import com.example.hanaro.domain.product.service.AdminProductService;
import com.example.hanaro.global.payload.response.ApiResponseDto;
import com.example.hanaro.global.swagger.annotations.product.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "CreateProductSingleImageMultipart")
    static class CreateProductSingleImageMultipart {
        @Schema(description = "상품 정보(JSON)", implementation = ProductCreateRequestDto.class)
        public ProductCreateRequestDto data;
        @Schema(type = "string", format = "binary", description = "상품 이미지 (최대 1장)")
        public MultipartFile image;
    }

    @Operation(
            summary = "상품 등록 (이미지 0 또는 1장)",
            description = "상품 정보(JSON)와 단일 이미지를 업로드합니다. 이미지가 없으면 DB에는 null로 저장됩니다."
    )
    @RequestBody(
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
    @AdminProductCreateApiResponses
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> createProduct(
            @Valid @RequestPart("data") ProductCreateRequestDto data,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        ProductResponseDto saved = adminProductService.createProduct(data, image);
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

    // ==== 수정: 이미지 안 보내면 유지, 보내면 교체 ====
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
    @AdminProductUpdateApiResponses
    @RequestBody(
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
    ) throws IOException {
        ProductResponseDto updated = adminProductService.updateProduct(id, req, image);
        return ResponseEntity.ok(ApiResponseDto.ok("상품이 수정되었습니다.", updated));
    }

    // ====== 재고만 수정 (PATCH) ======
    @Operation(
            summary = "상품 재고 수정 (관리자)",
            description = "상품 아이디와 재고를 받아 해당 상품의 재고를 갱신합니다.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductStockUpdateRequestDto.class),
                            examples = @ExampleObject(name = "재고 15로 변경", value = "{ \"stock\": 15 }")
                    )
            )
    )
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
}
