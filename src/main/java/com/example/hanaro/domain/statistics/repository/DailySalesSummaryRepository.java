package com.example.hanaro.domain.statistics.repository;

import com.example.hanaro.domain.statistics.entity.DailySalesSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DailySalesSummaryRepository extends JpaRepository<DailySalesSummary, LocalDate> {
}
