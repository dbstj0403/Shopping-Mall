package com.example.hanaro.domain.order.dto;

import com.example.hanaro.domain.order.entity.Order;
import com.example.hanaro.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        Long orderId,
        OrderStatus status,
        LocalDateTime createdAt,
        Integer totalQuantity,
        Integer totalAmount,
        List<OrderItemDto> items
) {
    public static OrderResponseDto from(Order o) {
        return new OrderResponseDto(
                o.getId(),
                o.getStatus(),
                o.getCreatedAt(),
                o.getTotalQuantity(),
                o.getTotalAmount(),
                o.getItems().stream().map(OrderItemDto::from).toList()
        );
    }
}
