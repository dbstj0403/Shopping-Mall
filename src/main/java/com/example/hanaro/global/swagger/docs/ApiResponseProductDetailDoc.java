package com.example.hanaro.global.swagger.docs;

import com.example.hanaro.domain.product.dto.ProductDetailDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponse<ProductDetailDto>", description = "API 표준 응답(상품 상세)")
public class ApiResponseProductDetailDoc {
    @Schema(example = "200")
    public int status;

    @Schema(example = "상품 상세 조회 성공")
    public String message;

    @Schema(nullable = true, example = "null")
    public String code;

    @Schema(implementation = ProductDetailDto.class)
    public ProductDetailDto data;
}
