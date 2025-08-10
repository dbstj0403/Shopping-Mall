// src/main/java/com/example/hanaro/domain/order/controller/AdminOrderController.java
package com.example.hanaro.domain.order.controller;

import com.example.hanaro.domain.order.dto.OrderListItemDto;
import com.example.hanaro.domain.order.service.OrderService;
import com.example.hanaro.global.payload.response.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@Tag(name = "ADMIN ORDER API", description = "관리자용 주문 목록 조회")
public class AdminOrderController {

    private final OrderService adminOrderService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "주문 전체 목록", description = "생성일 내림차순으로 전체 주문을 반환합니다. (유저/아이템 포함)")
    public ApiResponseDto<List<OrderListItemDto>> getAll() {
        return ApiResponseDto.ok(adminOrderService.getAll());
    }
}
