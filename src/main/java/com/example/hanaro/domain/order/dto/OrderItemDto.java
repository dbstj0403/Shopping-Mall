package com.example.hanaro.domain.order.dto;

import com.example.hanaro.domain.order.entity.OrderItem;

public record OrderItemDto(
        Long productId,
        String name,
        Integer unitPrice,
        Integer quantity,
        Integer lineTotal
) {
    public static OrderItemDto from(OrderItem oi) {
        return new OrderItemDto(
                oi.getProduct().getId(),
                oi.getProductName(),
                oi.getUnitPrice(),
                oi.getQuantity(),
                oi.getLineTotal()
        );
    }
}
