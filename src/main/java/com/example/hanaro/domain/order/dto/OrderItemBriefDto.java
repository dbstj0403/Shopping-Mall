// src/main/java/com/example/hanaro/domain/order/dto/OrderItemBriefDto.java
package com.example.hanaro.domain.order.dto;

import com.example.hanaro.domain.order.entity.OrderItem;

public record OrderItemBriefDto(
        Long productId,
        String productName,
        Integer quantity,
        Integer unitPrice,
        Integer subtotal
) {
    public static OrderItemBriefDto from(OrderItem oi) {
        // 단가 필드명이 price/orderPrice라면: oi.getPrice() 또는 oi.getOrderPrice()
        int unit = oi.getUnitPrice();
        return new OrderItemBriefDto(
                oi.getProduct().getId(),
                oi.getProduct().getName(),
                oi.getQuantity(),
                unit,
                oi.getQuantity() * unit
        );
    }
}
