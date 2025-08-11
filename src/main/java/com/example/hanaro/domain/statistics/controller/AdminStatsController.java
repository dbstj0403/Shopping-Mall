package com.example.hanaro.domain.statistics.controller;

import com.example.hanaro.domain.statistics.dto.DailyStatsDto;
import com.example.hanaro.domain.statistics.entity.DailySalesSummary;
import com.example.hanaro.domain.statistics.service.SalesAggregationJob;
import com.example.hanaro.domain.statistics.repository.DailySalesSummaryRepository;
import com.example.hanaro.global.payload.response.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
public class AdminStatsController {

    private final SalesAggregationJob job;
    private final DailySalesSummaryRepository summaryRepo;

    /** 특정 일자 재집계(관리자 수동 실행) */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/aggregate")
    public ResponseEntity<ApiResponseDto<Void>> aggregate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        job.aggregateFor(date);
        return ResponseEntity.ok(ApiResponseDto.ok("집계 요청 완료", null));
    }

    /** 특정 일자 총괄 조회 */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/daily")
    public ResponseEntity<ApiResponseDto<DailyStatsDto>> getDaily(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DailySalesSummary s = summaryRepo.findById(date)
                .orElseThrow(() -> new IllegalArgumentException("해당 일자 통계 없음"));
        var dto = new DailyStatsDto(s.getStatDate(), s.getTotalSales(), s.getTotalOrders());
        return ResponseEntity.ok(ApiResponseDto.ok(dto));
    }
}
