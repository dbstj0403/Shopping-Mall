// src/main/java/com/example/hanaro/domain/order/controller/AdminOrderController.java
package com.example.hanaro.domain.order.controller;

import com.example.hanaro.domain.order.dto.OrderListItemDto;
import com.example.hanaro.domain.order.service.OrderService;
import com.example.hanaro.global.payload.response.ApiResponseDto;
import com.example.hanaro.global.swagger.annotations.order.AdminOrderDetailApiResponses;
import com.example.hanaro.global.swagger.annotations.order.AdminOrderListApiResponses;
import com.example.hanaro.global.swagger.annotations.order.AdminOrderSearchApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@Tag(name = "ADMIN ORDER API", description = "관리자용 주문 목록 조회")
public class AdminOrderController {

    private final OrderService adminOrderService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @AdminOrderListApiResponses
    @Operation(summary = "주문 전체 목록", description = "전체 주문을 조회합니다.")
    public ApiResponseDto<List<OrderListItemDto>> getAll() {
        return ApiResponseDto.ok(adminOrderService.getAll());
    }

    // 단건: 주문 ID로 조회
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    @AdminOrderDetailApiResponses
    @Operation(summary = "주문 단건 조회", description = "아이디로 주문 단건을 조회합니다.")
    public ApiResponseDto<OrderListItemDto> getById(@PathVariable Long orderId) {
        return ApiResponseDto.ok(adminOrderService.getById(orderId));
    }

    // 검색: 상품명 포함으로 목록 조회
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    @AdminOrderSearchApiResponses
    @Operation(summary = "상품명으로 주문 검색", description = "상품명을 포함하는 주문들을 반환합니다.")
    public ApiResponseDto<List<OrderListItemDto>> searchByProductName(@RequestParam String productName) {
        return ApiResponseDto.ok(adminOrderService.searchByProductName(productName));
    }

}
