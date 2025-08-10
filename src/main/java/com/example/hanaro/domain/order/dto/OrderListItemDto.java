package com.example.hanaro.domain.order.dto;

import com.example.hanaro.domain.order.entity.Order;
import com.example.hanaro.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;

public record OrderListItemDto(
        Long orderId,
        OrderStatus status,
        LocalDateTime createdAt,
        Integer totalQuantity,
        Integer totalAmount
) {
    public static OrderListItemDto from(Order o) {
        return new OrderListItemDto(
                o.getId(),
                o.getStatus(),
                o.getCreatedAt(),
                o.getTotalQuantity(),
                o.getTotalAmount()
        );
    }
}
