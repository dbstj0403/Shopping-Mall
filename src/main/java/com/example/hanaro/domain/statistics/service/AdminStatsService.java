package com.example.hanaro.domain.statistics.service;

import com.example.hanaro.domain.statistics.dto.DailyStatsDto;
import com.example.hanaro.domain.statistics.entity.DailySalesSummary;
import com.example.hanaro.domain.statistics.repository.DailySalesSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminStatsService {

    private final DailySalesSummaryRepository summaryRepo;

    /** 일자 범위 총괄 목록 조회 (null이면 전체) */
    @Transactional(readOnly = true)
    public List<DailyStatsDto> listAllDaily(LocalDate from, LocalDate to) {
        List<DailySalesSummary> rows;
        if (from == null && to == null) {
            rows = summaryRepo.findAllByOrderByStatDateDesc();
        } else {
            LocalDate fromSafe = (from != null) ? from : LocalDate.of(1970, 1, 1);
            LocalDate toSafe   = (to   != null) ? to   : LocalDate.of(3000,12,31);
            rows = summaryRepo.findByStatDateBetweenOrderByStatDateDesc(fromSafe, toSafe);
        }

        return rows.stream()
                .map(s -> new DailyStatsDto(s.getStatDate(), s.getTotalSales(), s.getTotalOrders()))
                .toList();
    }
}
