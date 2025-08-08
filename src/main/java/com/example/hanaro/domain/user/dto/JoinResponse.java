package com.example.hanaro.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "회원가입 응답 데이터")
public class JoinResponse {
    @Schema(description = "생성된 회원 ID", example = "1")
    private Long userId;
}
