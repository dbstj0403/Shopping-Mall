package com.example.hanaro.global.swagger.annotations.product;

import com.example.hanaro.global.swagger.docs.ApiResponseErrorDoc;
import com.example.hanaro.global.swagger.docs.product.ApiResponseProductListDoc;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공",
                content = @Content(schema = @Schema(implementation = ApiResponseProductListDoc.class))),
        @ApiResponse(responseCode = "404", description = "상품 없음",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
                            { "status": 404, "message": "상품을 찾을 수 없습니다.", "code": "E004" }
                        """))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiResponseErrorDoc.class),
                        examples = @ExampleObject(value = """
                            { "status": 500, "message": "서버 내부 오류가 발생했습니다.", "code": "E999" }
                        """)))
})
public @interface ProductListApiResponses {}
