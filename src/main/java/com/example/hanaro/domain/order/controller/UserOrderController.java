package com.example.hanaro.domain.order.controller;

import com.example.hanaro.domain.order.dto.OrderListItemDto;
import com.example.hanaro.domain.order.dto.OrderResponseDto;
import com.example.hanaro.domain.order.service.OrderService;
import com.example.hanaro.global.payload.response.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "ORDER API", description = "주문 API")
public class UserOrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 생성(장바구니 기반)", description = "장바구니 아이템으로 주문을 생성합니다. 재고 부족 시 실패합니다.")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<OrderResponseDto>> create(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(
                ApiResponseDto.ok("주문이 생성되었습니다.", orderService.createFromCart(userId))
        );
    }

    @Operation(summary = "내 주문 목록", description = "본인이 주문한 내역을 최신순으로 조회합니다.")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<OrderListItemDto>>> list(@AuthenticationPrincipal Long userId) {
        var res = orderService.getMyOrders(userId);
        return ResponseEntity.ok(ApiResponseDto.ok("주문 목록 조회 성공", res));
    }

    @Operation(summary = "내 주문 상세", description = "내 주문 중 하나의 상세를 조회합니다.")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping(value = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<OrderResponseDto>> detail(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long orderId
    ) {
        OrderResponseDto res = orderService.getMyOrder(userId, orderId);
        return ResponseEntity.ok(ApiResponseDto.ok("주문 상세 조회 성공", res));
    }
}
