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
              "code": null,
              "data": [
                { "id": 1, "name": "티셔츠", "price": 10000, "stock": 5 },
                { "id": 2, "name": "모자",   "price": 15000, "stock": 2 }
              ]
            }
            """))),

        // 200 OK - 비어있는 목록(권장: 404 대신 이 방식)
        @ApiResponse(responseCode = "200", description = "상품이 없습니다(빈 목록)",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
            {
              "status": 200,
              "message": "상품이 없습니다",
              "data": []
            }
            """))),

        // 500 Internal Server Error
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "code": "E999" }
            """)))
})
public @interface ProductListApiResponses {}
