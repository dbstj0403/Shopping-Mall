package com.example.hanaro.global.swagger.annotations.order;

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
        // 200 OK
        @ApiResponse(responseCode = "200", description = "주문 생성 성공",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
            {
              "status": 200,
              "message": "주문이 생성되었습니다.",
              "data": {
                "orderId": 9,
                "status": "PAYMENT_COMPLETED",
                "createdAt": "2025-08-11T22:06:29.860245",
                "totalQuantity": 3,
                "totalAmount": 33333,
                "items": [
                  { "productId": 1, "name": "ipad", "unitPrice": 11111, "quantity": 3, "lineTotal": 33333 }
                ]
              }
            }
            """))),

        // 400 Bad Request (장바구니 비어 있음 등)
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = {
                                @ExampleObject(name = "EMPTY_CART", value = """
                { "status": 400, "message": "장바구니가 비어 있습니다.", "code": "E008" }
                """)
                        })),

        // 409 Conflict (재고 부족)
        @ApiResponse(responseCode = "409", description = "재고 부족/상태 충돌",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 409, "message": "재고 부족: ipad", "code": "E009" }
            """))),

        // 401 Unauthorized
        @ApiResponse(responseCode = "401", description = "인증 필요",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 401, "message": "인증이 필요합니다.", "code": "E002" }
            """))),

        // 403 Forbidden
        @ApiResponse(responseCode = "403", description = "권한 없음",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 403, "message": "접근 권한이 없습니다.", "code": "E003" }
            """))),

        // 500 Internal Server Error
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
            { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "code": "E999" }
            """)))
})
public @interface OrderCreateApiResponses {}
