package com.example.hanaro.global.swagger.docs;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponse<Error>", description = "API 표준 에러 응답")
public class ApiResponseErrorDoc {

    @Schema(example = "400")
    public int status;

    @Schema(example = "잘못된 요청입니다.")
    public String message;

    @Schema(example = "BAD_REQUEST")
    public String code;

    @Schema(nullable = true, example = "null")
    public Object data;
}
