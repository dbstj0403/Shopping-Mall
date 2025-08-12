package com.example.hanaro.global.swagger.annotations.order;

import com.example.hanaro.global.swagger.docs.ApiResponseErrorDoc;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "상품명으로 주문 검색 성공",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
      {
        "status": 200,
        "message": "성공",
        "data": [
          {
            "orderId": 6,
            "status": "DELIVERED",
            "createdAt": "2025-08-10T10:05:00",
            "userId": 1,
            "userName": "별돌이",
            "userEmail": "hanaro@email.com",
            "totalQuantity": 3,
            "totalAmount": 61222,
            "items": [
              { "productId": 1, "productName": "ipad", "quantity": 2, "unitPrice": 11111, "subtotal": 22222 },
              { "productId": 6, "productName": "모던 자바스크립트 딥다이브", "quantity": 1, "unitPrice": 39000, "subtotal": 39000 }
            ]
          }
        ]
      }
      """))),
        @ApiResponse(responseCode = "200", description = "검색 결과 없음",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
      {
        "status": 200,
        "message": "성공",
        "data": []
      }
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
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
      { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "code": "E999" }
      """)))
})
public @interface AdminOrderSearchApiResponses {}
