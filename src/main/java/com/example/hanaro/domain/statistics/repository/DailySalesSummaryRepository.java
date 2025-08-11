package com.example.hanaro.domain.statistics.repository;

import com.example.hanaro.domain.statistics.entity.DailySalesSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailySalesSummaryRepository extends JpaRepository<DailySalesSummary, LocalDate> {
    // 전체 최신순
    List<DailySalesSummary> findAllByOrderByStatDateDesc();

    // 기간 필터(옵션) + 최신순
    List<DailySalesSummary> findByStatDateBetweenOrderByStatDateDesc(LocalDate from, LocalDate to);
}
