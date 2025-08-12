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
        @ApiResponse(responseCode = "200", description = "주문 상세 조회 성공",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
            {
              "status": 200,
              "message": "주문 상세 조회 성공",
              "data": {
                "orderId": 1,
                "status": "DELIVERED",
                "createdAt": "2025-08-11T13:14:39.696324",
                "totalQuantity": 5,
                "totalAmount": 143444,
                "items": [
                  { "productId": 1, "name": "ipad", "unitPrice": 11111, "quantity": 4, "lineTotal": 44444 },
                  { "productId": 4, "name": "감자", "unitPrice": 99000, "quantity": 1, "lineTotal": 99000 }
                ]
              }
            }
            """))),

        // 404 Not Found
        @ApiResponse(responseCode = "404", description = "주문 없음",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
            { "status": 404, "message": "주문을 찾을 수 없습니다.", "code": "E004" }
            """))),

        // 401 Unauthorized
        @ApiResponse(responseCode = "401", description = "인증 필요",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
            { "status": 401, "message": "인증이 필요합니다.", "code": "E002" }
            """))),

        // 403 Forbidden
        @ApiResponse(responseCode = "403", description = "권한 없음",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
            { "status": 403, "message": "접근 권한이 없습니다.", "code": "E003" }
            """))),

        // 500 Internal Server Error
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
            { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "code": "E999" }
            """)))
})
public @interface OrderDetailApiResponses {}
