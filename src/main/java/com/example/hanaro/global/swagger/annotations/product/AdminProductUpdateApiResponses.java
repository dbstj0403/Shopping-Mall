package com.example.hanaro.global.swagger.annotations.product;

import com.example.hanaro.global.swagger.docs.ApiResponseErrorDoc;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "상품 수정 성공",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
      {
        "status": 200,
        "message": "상품이 수정되었습니다.",
        "data": {
          "id": 100,
          "name": "수정된 상품명",
          "price": 17000,
          "description": "수정된 설명",
          "stock": 12,
          "imageUrl": "https://example.com/img_new.png",
          "updatedAt": "2025-08-11T22:10:00"
        }
      }
      """))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청/검증 실패",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
      { "status": 400, "message": "요청 값이 유효하지 않습니다.", "code": "E001" }
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
        @ApiResponse(responseCode = "404", description = "상품 없음",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
      { "status": 404, "message": "상품을 찾을 수 없습니다.", "code": "E004" }
      """))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
      { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "code": "E999" }
      """)))
})
public @interface AdminProductUpdateApiResponses {}
