package com.example.hanaro.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "Name", example = "별돌이")
    private String name;

    @Schema(description = "Email", example = "hanaro@email.com")
    private String email;
}
