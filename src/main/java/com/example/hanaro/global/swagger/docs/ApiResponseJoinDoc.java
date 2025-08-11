package com.example.hanaro.global.swagger.docs;

import com.example.hanaro.domain.user.dto.JoinResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponse<JoinResponseDto>", description = "API 표준 응답(회원가입 성공)")
public class ApiResponseJoinDoc {

    @Schema(example = "200")
    public int status;

    @Schema(example = "회원가입이 성공적으로 완료되었습니다! 🎉")
    public String message;

    // 성공 시 null → JsonInclude.NON_NULL이면 실제 응답에선 제거됨
    @Schema(nullable = true, example = "null")
    public String code;

    @Schema(implementation = JoinResponseDto.class)
    public JoinResponseDto data;
}
