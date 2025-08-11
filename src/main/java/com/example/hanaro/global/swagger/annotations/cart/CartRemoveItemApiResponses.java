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
        @ApiResponse(responseCode = "200", description = "장바구니 아이템 삭제 성공",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
            {
              "status": 200,
              "message": "장바구니 아이템 삭제 성공",
              "data": {
                "items": [],
                "totalQuantity": 0,
                "totalAmount": 0
              }
            }
            """))),
        @ApiResponse(responseCode = "404", description = "장바구니 아이템 없음",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 404, "message": "장바구니 아이템을 찾을 수 없습니다.", "code": "E004" }
            """))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "code": "E999" }
            """)))
})
public @interface CartRemoveItemApiResponses {}
