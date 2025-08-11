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
        @ApiResponse(responseCode = "200", description = "장바구니 조회 성공",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
            {
              "status": 200,
              "message": "장바구니 조회 성공",
              "code": null,
              "data": {
                "totalQuantity": 3,
                "totalPrice": 35000,
                "items": [
                  { "productId": 1, "name": "티셔츠", "price": 10000, "quantity": 2, "lineTotal": 20000 },
                  { "productId": 2, "name": "모자", "price": 15000, "quantity": 1, "lineTotal": 15000 }
                ]
              }
            }
            """))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "code": "E999" }
            """)))
})
public @interface CartGetApiResponses {}
