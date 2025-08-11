package com.example.hanaro.domain.order.service;

import com.example.hanaro.domain.order.entity.OrderStatus;
import com.example.hanaro.domain.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderStatusScheduler {

    private final OrderRepository orderRepository;

    // 매 분 실행해서 "5분 지난 결제완료 → 배송준비"
    @Scheduled(cron = "0 * * * * *") // 매 분 0초
    public void paymentCompletedToPreparingShipment() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(5);
        int updated = orderRepository.bulkAdvance(
                OrderStatus.PAYMENT_COMPLETED, OrderStatus.PREPARING_SHIPMENT, cutoff);
        log(updated, "PAYMENT_COMPLETED → PREPARING_SHIPMENT");
    }

    // 매 분 실행해서 "15분 지난 배송준비 → 배송중"
    @Scheduled(cron = "10 * * * * *") // 매 분 10초 (충돌 방지로 약간 시차)
    public void preparingShipmentToInTransit() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(15);
        int updated = orderRepository.bulkAdvance(
                OrderStatus.PREPARING_SHIPMENT, OrderStatus.IN_TRANSIT, cutoff);
        log(updated, "PREPARING_SHIPMENT → IN_TRANSIT");
    }

    // 매 5분 실행해서 "1시간 지난 배송중 → 배송완료"
    @Scheduled(cron = "20 */5 * * * *") // 5분마다 20초
    public void inTransitToDelivered() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(1);
        int updated = orderRepository.bulkAdvance(
                OrderStatus.IN_TRANSIT, OrderStatus.DELIVERED, cutoff);
        log(updated, "IN_TRANSIT → DELIVERED");
    }

    private void log(int n, String label) {
        if (n > 0) {
            System.out.println("[OrderStatusScheduler] " + label + " : " + n + "건");
        }
    }
}
