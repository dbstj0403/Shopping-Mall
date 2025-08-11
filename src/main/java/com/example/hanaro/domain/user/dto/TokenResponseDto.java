package com.example.hanaro.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "발급된 JWT 토큰 응답")
public record TokenResponseDto(
        @Schema(description = "JWT 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token
) {}