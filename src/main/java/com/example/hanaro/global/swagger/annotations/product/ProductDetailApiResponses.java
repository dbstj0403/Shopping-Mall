package com.example.hanaro.global.swagger.annotations.product;

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
        // 200 OK
        @ApiResponse(responseCode = "200", description = "상품 상세 조회 성공",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
                {
                  "status": 200,
                  "message": "상품 상세 조회 성공",
                  "data": {
                    "name": "샘플 상품",
                    "price": 15000,
                    "description": "이 상품은 예시 상품입니다."
                  }
                }
            """))),

        // 404 Not Found
        @ApiResponse(responseCode = "404", description = "상품 없음",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
                {
                  "status": 404,
                  "message": "상품을 찾을 수 없습니다.",
                  "code": "E004"
                }
            """))),

        // 500 Internal Server Error
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
                {
                  "status": 500,
                  "message": "서버 내부 오류가 발생했습니다.",
                  "code": "E999"
                }
            """)))
})
public @interface ProductDetailApiResponses {}
