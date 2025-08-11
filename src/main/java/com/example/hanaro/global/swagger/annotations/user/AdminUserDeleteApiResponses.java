package com.example.hanaro.global.swagger.annotations.user;

import com.example.hanaro.global.swagger.docs.ApiResponseErrorDoc;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        // 204 No Content (성공 시 바디 없음)
        @ApiResponse(responseCode = "204", description = "삭제 성공"),

        // 401 Unauthorized
        @ApiResponse(responseCode = "401", description = "인증 필요",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 401, "message": "인증이 필요합니다.", "code": "E002" }
            """))),

        // 403 Forbidden
        @ApiResponse(responseCode = "403", description = "권한 없음",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 403, "message": "접근 권한이 없습니다.", "code": "E003" }
            """))),

        // 404 Not Found
        @ApiResponse(responseCode = "404", description = "회원 없음",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 404, "message": "회원을 찾을 수 없습니다.", "code": "E004" }
            """))),

        // 500 Internal Server Error
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "code": "E999" }
            """)))
})
public @interface AdminUserDeleteApiResponses {}
