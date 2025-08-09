package com.example.hanaro.global.payload.response;

import com.example.hanaro.global.error.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // null인 필드는 JSON에서 제외
@Schema(name = "ApiResponseDto", description = "API 표준 응답 형식")
public class ApiResponseDto<T> {

    @Schema(description = "HTTP 상태 코드", example = "200")
    private int status;

    @Schema(description = "응답 메시지", example = "성공")
    private String message;

    @Schema(description = "에러 코드(실패 시만 사용)")
    private String code;

    @Schema(description = "응답 데이터")
    private T data;

    public static <T> ApiResponseDto<T> ok(T data) {
        return new ApiResponseDto<>(200, "성공", null, data);
    }

    public static <T> ApiResponseDto<T> ok(String message, T data) {
        return new ApiResponseDto<>(200, message, null, data);
    }

    public static <T> ApiResponseDto<T> fail(ErrorCode ec) {
        return new ApiResponseDto<>(ec.getStatus().value(), ec.getMessage(), ec.getCode(), null);
    }

    public static <T> ApiResponseDto<T> fail(ErrorCode ec, String overrideMessage) {
        return new ApiResponseDto<>(ec.getStatus().value(), overrideMessage, ec.getCode(), null);
    }
}
