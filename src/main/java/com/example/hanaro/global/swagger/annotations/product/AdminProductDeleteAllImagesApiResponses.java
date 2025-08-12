package com.example.hanaro.global.swagger.annotations.product;

import com.example.hanaro.domain.product.dto.ProductResponseDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "상품 이미지 전체 삭제 성공",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ProductResponseDto.class),
                        examples = @ExampleObject(value = """
                                {
                                  "status": 200,
                                  "message": "상품 이미지가 모두 삭제되었습니다.",
                                  "data": {
                                    "id": 1,
                                    "name": "초코 쿠키",
                                    "price": 2500,
                                    "description": "달콤달콤 수제 쿠키",
                                    "stock": 10,
                                    "imageUrls": []
                                  }
                                }
                                """)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "상품 없음",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
                                {
                                  "status": 404,
                                  "message": "상품을 찾을 수 없습니다. id=999",
                                  "code": "NOT_FOUND"
                                }
                                """)
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "서버 내부 오류",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
                                {
                                  "status": 500,
                                  "message": "서버 내부 오류가 발생했습니다.",
                                  "code": "INTERNAL_SERVER_ERROR"
                                }
                                """)
                )
        )
})
public @interface AdminProductDeleteAllImagesApiResponses {
}
