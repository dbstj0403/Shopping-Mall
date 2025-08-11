 package com.example.hanaro.global.swagger.annotations.user;

import com.example.hanaro.global.swagger.docs.user.ApiResponseJoinDoc;
import com.example.hanaro.global.swagger.docs.ApiResponseErrorDoc;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원가입 성공",
                content = @Content(schema = @Schema(implementation = ApiResponseJoinDoc.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 400, "message": "잘못된 요청입니다.", "code": "E001" }
            """))),
        @ApiResponse(responseCode = "409", description = "이메일 중복",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 409, "message": "이미 사용 중인 이메일입니다.", "code": "E006" }
            """))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "code": "E999" }
            """)))
})
public @interface JoinApiResponses {}
