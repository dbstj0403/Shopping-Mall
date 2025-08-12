package com.example.hanaro.global.swagger.annotations.user;

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
        // 200 OK
        @ApiResponse(responseCode = "200", description = "회원 목록 조회 성공",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
            {
              "status": 200,
              "message": "성공",
              "data": [
                { "id": 1,  "name": "별돌이", "email": "hanaro@email.com" },
                { "id": 6,  "name": "별송이", "email": "admin@email.com" },
                { "id": 7,  "name": "별벗",   "email": "hanaro2@email.com" },
                { "id": 8,  "name": "별봄이", "email": "hanaro3@email.com" },
                { "id": 9,  "name": "별프로", "email": "hanaro4@email.com" },
                { "id": 11, "name": "별누리", "email": "hanaro6@email.com" },
                { "id": 12, "name": "별별이", "email": "hanaro7@email.com" },
                { "id": 13, "name": "hana",  "email": "hana@email.com" }
              ]
            }
            """))),

        // 401 Unauthorized
        @ApiResponse(responseCode = "401", description = "인증 필요",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
            { "status": 401, "message": "인증이 필요합니다.", "code": "E002" }
            """))),

        // 403 Forbidden
        @ApiResponse(responseCode = "403", description = "권한 없음",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
            { "status": 403, "message": "접근 권한이 없습니다.", "code": "E003" }
            """))),

        // 500 Internal Server Error
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
            { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "code": "E999" }
            """)))
})
public @interface AdminUserListApiResponses {}
