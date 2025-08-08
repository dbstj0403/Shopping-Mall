package com.example.hanaro.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "API 표준 응답 형식")
public class ApiResponseDto<T> {
    @Schema(description = "HTTP 상태 코드", example = "200")
    private int status;

    @Schema(description = "응답 메시지", example = "회원가입이 성공적으로 완료되었습니다! 🎉")
    private String message;

    @Schema(description = "응답 데이터")
    private T data;
}
