package com.example.hanaro.domain.statistics.service;

import com.example.hanaro.domain.statistics.entity.DailySalesSummary;
import com.example.hanaro.domain.statistics.entity.ProductDailySales;
import com.example.hanaro.domain.statistics.repository.AggOrderItemRepository;
import com.example.hanaro.domain.statistics.repository.AggOrderRepository;
import com.example.hanaro.domain.statistics.repository.DailySalesSummaryRepository;
import com.example.hanaro.domain.statistics.repository.ProductDailySalesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesAggregationJob {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final AggOrderRepository orderAggRepo;
    private final AggOrderItemRepository orderItemAggRepo;
    private final DailySalesSummaryRepository summaryRepo;
    private final ProductDailySalesRepository productRepo;

    /**
     * 매일 자정(KST) 실행: 어제(KST)의 데이터를 집계.
     * 누락된 날짜가 있으면 연속으로 메꿔서 집계한다.
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void runDailyAggregation() {
        LocalDate todayKst = LocalDate.now(KST);
        LocalDate expectedTarget = todayKst.minusDays(1); // 어제(KST)

        // 마지막 집계일 조회 (없으면 expectedTarget의 전날로 가정해서 어제만 집계)
        LocalDate lastAggregated = summaryRepo.findTopByOrderByStatDateDesc()
                .map(DailySalesSummary::getStatDate)
                .orElse(expectedTarget.minusDays(1));

        // last+1 부터 expectedTarget 까지 순차 집계 (누락 복구)
        for (LocalDate d = lastAggregated.plusDays(1); !d.isAfter(expectedTarget); d = d.plusDays(1)) {
            aggregateFor(d);
        }

        if (lastAggregated.isAfter(expectedTarget)) {
            // 서버 시간/프로필 오설정 등으로 미래를 집계해둔 흔적
            log.warn("[WARN] lastAggregated={} is after expectedTarget={} (check server clock/timezone)",
                    lastAggregated, expectedTarget);
        }
    }

    @Transactional
    public void aggregateFor(LocalDate statDate) {
        // === 집계 구간 계산 ===
        // KST 기준 하루의 시작/끝을 잡고, DB가 UTC 저장이면 UTC로 변환해서 쿼리 범위에 사용
        ZonedDateTime kstStartZ = statDate.atStartOfDay(KST);
        ZonedDateTime kstEndZ   = statDate.plusDays(1).atStartOfDay(KST);

        // (A) DB가 KST(LocalDateTime)로 저장/비교되는 경우:
        LocalDateTime start = kstStartZ.toLocalDateTime();
        LocalDateTime end   = kstEndZ.toLocalDateTime();

        // 멱등성: 이미 있으면 스킵
        if (summaryRepo.existsById(statDate)) {
            log.info("[SKIP] already aggregated: {}", statDate);
            return;
        }

        // 1) 일자 총괄
        var totals = orderAggRepo.sumDayAndCount(start, end);
        long totalSales = totals.getTotalSales() == null ? 0L : totals.getTotalSales().longValue();
        int  totalOrders = totals.getTotalOrders() == null ? 0  : totals.getTotalOrders().intValue();
        summaryRepo.save(new DailySalesSummary(statDate, totalSales, totalOrders));

        // 2) 상품별
        var perProduct = orderItemAggRepo.aggregatePerProduct(start, end);
        var entities = perProduct.stream()
                .map(a -> ProductDailySales.builder()
                        .statDate(statDate)
                        .productId(a.getProductId())
                        .totalQty(a.getQty() == null ? 0 : a.getQty().intValue())
                        .totalSales(a.getSales() == null ? 0L : a.getSales())
                        .build())
                .toList();
        productRepo.saveAll(entities);

        log.info("[OK] aggregated: {}  totalSales={}, totalOrders={}, productRows={}",
                statDate, totalSales, totalOrders, entities.size());
    }
}
