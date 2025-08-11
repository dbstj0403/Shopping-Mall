package com.example.hanaro.global.swagger.annotations.order;

import com.example.hanaro.global.swagger.docs.ApiResponseErrorDoc;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "주문 전체 목록 조회 성공",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
      {
        "status": 200,
        "message": "성공",
        "data": [
          {
            "orderId": 8,
            "status": "DELIVERED",
            "createdAt": "2025-08-10T21:45:00",
            "userId": 1,
            "userName": "별돌이",
            "userEmail": "hanaro@email.com",
            "totalQuantity": 3,
            "totalAmount": 288000,
            "items": [
              { "productId": 5, "productName": "아이폰", "quantity": 2, "unitPrice": 99000, "subtotal": 198000 },
              { "productId": 10, "productName": "보노보노", "quantity": 1, "unitPrice": 90000, "subtotal": 90000 }
            ]
          },
          {
            "orderId": 7,
            "status": "DELIVERED",
            "createdAt": "2025-08-10T13:20:00",
            "userId": 1,
            "userName": "별돌이",
            "userEmail": "hanaro@email.com",
            "totalQuantity": 1,
            "totalAmount": 99000,
            "items": [
              { "productId": 4, "productName": "감자", "quantity": 1, "unitPrice": 99000, "subtotal": 99000 }
            ]
          }
        ]
      }
      """))),
        @ApiResponse(responseCode = "401", description = "인증 필요",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
      { "status": 401, "message": "인증이 필요합니다.", "code": "E002" }
      """))),
        @ApiResponse(responseCode = "403", description = "권한 없음",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
      { "status": 403, "message": "접근 권한이 없습니다.", "code": "E003" }
      """))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
      { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "code": "E999" }
      """)))
})
public @interface AdminOrderListApiResponses {}
