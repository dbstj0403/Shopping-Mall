package com.example.hanaro.global.swagger.annotations.product;

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
        // 201 Created (본문 status는 200 형식 사용)
        @ApiResponse(responseCode = "201", description = "상품 등록 성공",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
      {
        "status": 200,
        "message": "상품이 등록되었습니다.",
        "data": {
          "id": 100,
          "name": "샘플 상품",
          "price": 15000,
          "description": "예시 설명",
          "stock": 10,
          "images": [
            "/static/upload/2025/08/12/abcd1.jpeg",
            "/static/upload/2025/08/12/abcd2.jpeg"
          ],
          "createdAt": "2025-08-11T22:06:29.860245"
        }
      }
      """))),

        // 400 Bad Request (data 필드 없음)
        @ApiResponse(responseCode = "400", description = "잘못된 요청/검증 실패",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
      { "status": 400, "message": "요청 값이 유효하지 않습니다.", "code": "E001" }
      """))),

        // 401 Unauthorized (data 필드 없음)
        @ApiResponse(responseCode = "401", description = "인증 필요",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
      { "status": 401, "message": "인증이 필요합니다.", "code": "E002" }
      """))),

        // 403 Forbidden (data 필드 없음)
        @ApiResponse(responseCode = "403", description = "권한 없음",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
      { "status": 403, "message": "접근 권한이 없습니다.", "code": "E003" }
      """))),

        // 500 Internal Server Error (data 필드 없음)
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
      { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "code": "E999" }
      """)))
})
public @interface AdminProductCreateApiResponses {}
