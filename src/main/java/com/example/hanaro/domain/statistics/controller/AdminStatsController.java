// src/main/java/com/example/hanaro/domain/statistics/controller/AdminStatsController.java
package com.example.hanaro.domain.statistics.controller;

import com.example.hanaro.domain.statistics.dto.DailyStatsDto;
import com.example.hanaro.domain.statistics.service.AdminStatsService;
import com.example.hanaro.global.payload.response.ApiResponseDto;
import com.example.hanaro.global.swagger.annotations.statistics.AdminStatsDailyListApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "STATISTICS API", description = "관리자용 매출 조회 API입니다.")
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    /** 일자 범위 총괄 목록 조회 (미지정 시 전체) */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "매출 통계 조회", description = "해당하는 기간의 매출 통계를 조회합니다. 아무것도 보내지 않고 조회시 모든 통계가 반환됩니다.")
    @GetMapping("/daily/all")
    @AdminStatsDailyListApiResponses
    public ResponseEntity<ApiResponseDto<List<DailyStatsDto>>> listAllDaily(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        var body = adminStatsService.listAllDaily(from, to);
        return ResponseEntity.ok(ApiResponseDto.ok(body));
    }
}
