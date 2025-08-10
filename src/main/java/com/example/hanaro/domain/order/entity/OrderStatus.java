package com.example.hanaro.domain.order.entity;

public enum OrderStatus {
    PAYMENT_COMPLETED,   // 결제 완료
    PREPARING_SHIPMENT,  // 배송 준비
    IN_TRANSIT,          // 배송 중
    DELIVERED           // 배송 완료
}
