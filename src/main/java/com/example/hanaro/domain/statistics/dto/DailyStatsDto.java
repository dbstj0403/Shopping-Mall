package com.example.hanaro.domain.statistics.dto;

import java.time.LocalDate;

public record DailyStatsDto(
        LocalDate statDate,
        long totalSales,
        int totalOrders
) {}
