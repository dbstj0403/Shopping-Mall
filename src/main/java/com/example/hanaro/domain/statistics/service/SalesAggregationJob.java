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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesAggregationJob {

    private final AggOrderRepository orderAggRepo;
    private final AggOrderItemRepository orderItemAggRepo;
    private final DailySalesSummaryRepository summaryRepo;
    private final ProductDailySalesRepository productRepo;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void runDailyAggregation() {
        LocalDate target = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);
        aggregateFor(target);
    }

    @Transactional
    public void aggregateFor(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.plusDays(1).atStartOfDay();

        // 멱등성: 이미 있으면 스킵
        if (summaryRepo.findById(date).isPresent()) {
            log.info("[SKIP] already aggregated: {}", date);
            return;
        }

        // 1) 일자 총괄 (상태 필터 없음)
        var totals = orderAggRepo.sumDayAndCount(start, end); // DayTotals 프로젝션
        long totalSales = totals.getTotalSales() == null ? 0L : totals.getTotalSales().longValue();
        int  totalOrders = totals.getTotalOrders() == null ? 0  : totals.getTotalOrders().intValue();
        summaryRepo.save(new DailySalesSummary(date, totalSales, totalOrders));

        // 2) 상품별 (상태 필터 없음)
        var perProduct = orderItemAggRepo.aggregatePerProduct(start, end);
        var entities = perProduct.stream()
                .map(a -> ProductDailySales.builder()
                        .statDate(date)
                        .productId(a.getProductId())
                        .totalQty(a.getQty().intValue())
                        .totalSales(a.getSales())
                        .build())
                .toList();
        productRepo.saveAll(entities);

        log.info("[OK] aggregated: {}  totalSales={}, totalOrders={}, productRows={}",
                date, totalSales, totalOrders, entities.size());
    }
}
