package com.example.hanaro.domain.order.dto;

import com.example.hanaro.domain.order.entity.Order;
import com.example.hanaro.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderListItemDto(
        Long orderId,
        OrderStatus status,
        LocalDateTime createdAt,
        Long userId,
        String userName,
        String userEmail,
        int totalQuantity,
        int totalAmount,
        List<OrderItemBriefDto> items
) {
    public static OrderListItemDto from(Order o) {
        List<OrderItemBriefDto> itemDtos = o.getItems()
                .stream()
                .map(OrderItemBriefDto::from)
                .toList();

        return new OrderListItemDto(
                o.getId(),
                o.getStatus(),
                o.getCreatedAt(),
                o.getUser().getId(),
                o.getUser().getName(),
                o.getUser().getEmail(),
                o.getTotalQuantity(),   // int라 null 체크 불필요
                o.getTotalAmount(),     // int라 null 체크 불필요
                itemDtos
        );
    }
}
