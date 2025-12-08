package com.codeit.sb06deokhugamteam2.dashboard.repository;

import com.codeit.sb06deokhugamteam2.common.enums.PeriodType;
import com.codeit.sb06deokhugamteam2.common.enums.RankingType;
import com.codeit.sb06deokhugamteam2.dashboard.entity.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.UUID;

public interface DashboardRepository extends JpaRepository<Dashboard, UUID>, DashboardRepositoryCustom {
    long countByRankingTypeAndPeriodTypeAndCreatedAtBetween(
            RankingType rankingType, PeriodType periodType,
            Instant startDate, Instant endDate
    );
}
