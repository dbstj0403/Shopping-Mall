package com.example.hanaro.global.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "API 표준 응답 형식")
public class ApiResponseDto<T> {
    @Schema(description = "HTTP 상태 코드", example = "200")
    private int status;

    @Schema(description = "응답 메시지", example = "회원가입이 성공적으로 완료되었습니다! 🎉")
    private String message;

    @Schema(description = "응답 데이터")
    private T data;

    // 정적 팩토리 (컨트롤러, 핸들러에서 편하게 사용하기 위함)
    public static <T> ApiResponseDto<T> ok(T data) {
        return new ApiResponseDto<>(200, "성공", data);
    }
    public static <T> ApiResponseDto<T> ok(String message, T data) {
        return new ApiResponseDto<>(200, message, data);
    }
    public static <T> ApiResponseDto<T> fail(int status, String message) {
        return new ApiResponseDto<>(status, message, null);
    }
    public static <T> ApiResponseDto<T> fail(int status, String message, T data) {
        return new ApiResponseDto<>(status, message, data);
    }

}
