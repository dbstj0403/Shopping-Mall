 package com.example.hanaro.global.swagger.docs;

import com.example.hanaro.domain.product.dto.ProductListItemDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

 @Schema(name = "ApiResponse<ProductList>", description = "상품 목록 응답")
 public class ApiResponseProductListDoc {
     @Schema(example = "200")
     public int status;

     @Schema(example = "상품 목록 조회 성공")
     public String message;

     @Schema(nullable = true, example = "null")
     public String code;

     @Schema(example = "[{\"name\":\"상품1\", \"price\": 10000}]")
     public Object data;
 }

