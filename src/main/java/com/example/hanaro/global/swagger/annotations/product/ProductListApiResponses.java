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
        // 200 OK - 정상 목록
        @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
            {
              "status": 200,
              "message": "상품 목록 조회 성공",
              "data": [
                {
                  "id": 21,
                  "name": "웰시코기",
                  "price": 23333,
                  "description": "귀엽",
                  "stock": 5664,
                  "images": [
                    "/static/upload/2025/08/12/a.png",
                    "/static/upload/2025/08/12/b.png"
                  ]
                },
                {
                  "id": 20,
                  "name": "에어팟",
                  "price": 200000,
                  "description": "Apple",
                  "stock": 26,
                  "images": [
                    "/static/upload/2025/08/12/c.png"
                  ]
                }
              ]
            }
            """))),

        // 200 OK - 비어있는 목록
        @ApiResponse(responseCode = "200", description = "상품이 없습니다. (빈 목록)",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
            {
              "status": 200,
              "message": "상품이 없습니다",
              "data": []
            }
            """))),

        // 500 Internal Server Error (data 필드 없음)
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
            {
              "status": 500,
              "message": "서버 내부 오류가 발생했습니다.",
              "code": "E999"
            }
            """)))
})
public @interface ProductListApiResponses {}
