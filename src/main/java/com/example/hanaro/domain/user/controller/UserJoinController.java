package com.example.hanaro.domain.user.controller;

import com.example.hanaro.domain.user.dto.JoinResponseDto;
import com.example.hanaro.domain.user.dto.JoinRequestDto;
import com.example.hanaro.domain.user.service.UserService;
import com.example.hanaro.global.payload.response.ApiResponseDto;
import com.example.hanaro.global.swagger.examples.UserExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "USER API", description = "회원 관련 API입니다.")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserJoinController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "유효성 검사를 포함한 회원가입 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class),
                            examples = @ExampleObject(value = UserExamples.JOIN_SUCCESS))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class),
                            examples = @ExampleObject(value = UserExamples.JOIN_VALIDATION_ERROR))),
            @ApiResponse(responseCode = "409", description = "이메일 중복",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class),
                            examples = @ExampleObject(value = UserExamples.JOIN_EMAIL_DUP)))
    })
    @PostMapping(
            value = "/join",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<ApiResponseDto<JoinResponseDto>> signup(@Valid @RequestBody JoinRequestDto request) {
        Long userId = userService.join(request);
        return ResponseEntity.ok(
                ApiResponseDto.ok("회원가입이 성공적으로 완료되었습니다! 🎉", new JoinResponseDto(userId))
        );
    }
}
