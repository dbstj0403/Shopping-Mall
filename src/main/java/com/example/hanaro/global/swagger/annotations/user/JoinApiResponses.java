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
        @ApiResponse(responseCode = "200", description = "회원가입 성공",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(
                                type = "object",
                                example = """
                {
                  "status": 200,
                  "message": "회원가입이 성공적으로 완료되었습니다.",
                  "data": { "userId": 1001 }
                }
                """
                        )
                )
        ),

        // 400 Bad Request
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
            { "status": 400, "message": "잘못된 요청입니다.", "code": "E001" }
            """))
        ),

        // 409 Conflict (이메일 중복)
        @ApiResponse(responseCode = "409", description = "이메일 중복",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
            { "status": 409, "message": "이미 사용 중인 이메일입니다.", "code": "E006" }
            """))
        ),

        // 500 Internal Server Error
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
            { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "code": "E999" }
            """))
        )
})
public @interface JoinApiResponses {}
