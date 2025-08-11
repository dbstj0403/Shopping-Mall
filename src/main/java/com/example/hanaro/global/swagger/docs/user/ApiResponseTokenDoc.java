package com.example.hanaro.global.swagger.docs.user;

import com.example.hanaro.domain.user.dto.TokenResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponse<TokenResponseDto>", description = "API 표준 응답(토큰)")
public class ApiResponseTokenDoc {
    @Schema(example = "200")
    public int status;

    @Schema(example = "성공")
    public String message;

    @Schema(description = "에러 코드(실패 시만 사용)", example = "null")
    public String code;

    @Schema(description = "응답 데이터")
    public TokenResponseDto data;
}
