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
        @ApiResponse(responseCode = "200", description = "장바구니 수정 성공",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
            {
              "status": 200,
              "message": "장바구니 수정 성공",
              "data": {
                "items": [
                  {
                    "productId": 1,
                    "name": "ipad",
                    "price": 11111,
                    "quantity": 3,
                    "lineTotal": 33333
                  }
                ],
                "totalQuantity": 3,
                "totalAmount": 33333
              }
            }
            """))),
        @ApiResponse(responseCode = "400", description = "잘못된 수량",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
            { "status": 400, "message": "유효하지 않은 수량입니다.", "code": "E010" }
            """))),
        @ApiResponse(responseCode = "404", description = "상품/장바구니 아이템 없음",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
            { "status": 404, "message": "장바구니 아이템을 찾을 수 없습니다.", "code": "E004" }
            """))),
        @ApiResponse(responseCode = "409", description = "재고 부족",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
            { "status": 409, "message": "재고가 부족합니다.", "code": "E005" }
            """))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
            { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "code": "E999" }
            """)))
})
public @interface CartUpdateQtyApiResponses {}
