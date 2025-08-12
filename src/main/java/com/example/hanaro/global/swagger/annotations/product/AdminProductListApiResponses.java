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
        @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
      {
        "status": 200,
        "message": "상품 목록 조회 성공",
        "data": [
          {
            "id": 1,
            "name": "티셔츠",
            "price": 10000,
            "description": "면 100%",
            "stock": 5,
            "images": []
          },
          {
            "id": 2,
            "name": "모자",
            "price": 15000,
            "description": "여름용",
            "stock": 2,
            "images": [
              "/static/upload/2025/08/12/hat.png"
            ]
          }
        ]
      }
      """))),

        @ApiResponse(responseCode = "401", description = "인증 필요",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
      { "status": 401, "message": "인증이 필요합니다.", "code": "E002" }
      """))),

        @ApiResponse(responseCode = "403", description = "권한 없음",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
      { "status": 403, "message": "접근 권한이 없습니다.", "code": "E003" }
      """))),

        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
      { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "code": "E999" }
      """)))
})
public @interface AdminProductListApiResponses {}
