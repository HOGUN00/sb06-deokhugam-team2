package com.codeit.sb06deokhugamteam2.dashboard.repository;

import com.codeit.sb06deokhugamteam2.dashboard.dto.data.PopularReviewDto;
import com.codeit.sb06deokhugamteam2.dashboard.entity.Dashboard;
import com.codeit.sb06deokhugamteam2.common.enums.PeriodType;
import com.codeit.sb06deokhugamteam2.common.enums.RankingType;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.List;

public interface DashboardRepositoryCustom {

    List<Dashboard> findPopularBookListByCursor(
            RankingType rankingType,
            PeriodType period,
            String cursor,
            Instant after,
            Sort.Direction direction,
            Integer limit
    );

    Slice<PopularReviewDto> findPopularReviews(
            PeriodType periodType,
            String direction,
            String cursor,
            String after,
            int limit,
            Instant startDate,
            Instant endDate
    );
}
