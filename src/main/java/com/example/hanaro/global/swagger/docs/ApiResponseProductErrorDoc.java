package com.example.hanaro.global.swagger.docs;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponse<Error(Product)>", description = "상품 API 전용 에러 응답")
public class ApiResponseProductErrorDoc {

    @Schema(example = "404")
    public int status;

    @Schema(example = "상품을 찾을 수 없습니다.")
    public String message;

    @Schema(example = "E004")
    public String code;

    @Schema(nullable = true, example = "null")
    public Object data;
}
