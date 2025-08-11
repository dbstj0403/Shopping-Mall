package com.example.hanaro.global.swagger.annotations.cart;

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
        @ApiResponse(responseCode = "200", description = "장바구니에 상품 담기 성공",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
            {
              "status": 200,
              "message": "장바구니 담기 성공",
              "code": null,
              "data": {
                "productId": 1,
                "name": "티셔츠",
                "price": 10000,
                "quantity": 2,
                "lineTotal": 20000,
                "totalQuantity": 3,
                "totalPrice": 35000
              }
            }
            """))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청(수량 1 미만 등)",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 400, "message": "유효하지 않은 수량입니다.", "code": "E010" }
            """))),
        @ApiResponse(responseCode = "404", description = "상품 없음",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 404, "message": "상품을 찾을 수 없습니다.", "code": "E004" }
            """))),
        @ApiResponse(responseCode = "409", description = "재고 부족",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 409, "message": "재고가 부족합니다.", "code": "E005" }
            """))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "code": "E999" }
            """)))
})
public @interface CartAddItemApiResponses {}
