package com.example.hanaro.domain.user.controller;

import com.example.hanaro.domain.user.dto.JoinResponse;
import com.example.hanaro.domain.user.dto.UserJoinRequest;
import com.example.hanaro.domain.user.service.UserService;
import com.example.hanaro.global.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "USER API", description = "회원 관련 API입니다.")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "유효성 검사를 포함한 회원가입 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"status\": 200,\n" +
                                            "  \"message\": \"회원가입이 성공적으로 완료되었습니다! 🎉\",\n" +
                                            "  \"data\": {\n" +
                                            "    \"userId\": 1\n" +
                                            "  }\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "입력값 오류 혹은 유효성 검사 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"status\": 400,\n" +
                                            "  \"message\": \"이메일 형식이 올바르지 않습니다.\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"status\": 500,\n" +
                                            "  \"message\": \"서버 내부 오류가 발생했습니다.\"\n" +
                                            "}"
                            )
                    )
            )
    })
    @PostMapping("/join")
    public ApiResponseDto<JoinResponse> signup(@RequestBody UserJoinRequest request) {
        Long userId = userService.join(request);
        return new ApiResponseDto<>(200, "회원가입이 성공적으로 완료되었습니다! 🎉", new JoinResponse(userId));
    }
}
