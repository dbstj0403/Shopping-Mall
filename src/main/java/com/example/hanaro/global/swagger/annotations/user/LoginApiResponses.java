package com.example.hanaro.global.swagger.annotations.user;

import com.example.hanaro.global.swagger.docs.ApiResponseErrorDoc;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        // 200 OK (스키마 type=object + example -> JSON으로 렌더)
        @ApiResponse(responseCode = "200", description = "로그인 성공",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(
                                type = "object",
                                example = """
                {
                  "status": 200,
                  "message": "로그인 성공",
                  "data": {
                    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                  }
                }
                """
                        )
                )
        ),

        // 400 Bad Request
        @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 자격증명 불일치",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 400, "message": "이메일 또는 비밀번호가 올바르지 않습니다.", "code": "E007" }
            """))
        ),

        // 500 Internal Server Error
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "code": "E999" }
            """))
        )
})
public @interface LoginApiResponses {}
