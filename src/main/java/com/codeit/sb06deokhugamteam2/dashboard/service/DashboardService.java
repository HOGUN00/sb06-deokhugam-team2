package com.codeit.sb06deokhugamteam2.dashboard.service;

import com.codeit.sb06deokhugamteam2.common.enums.PeriodType;
import com.codeit.sb06deokhugamteam2.common.enums.RankingType;
import com.codeit.sb06deokhugamteam2.dashboard.dto.data.PopularReviewDto;
import com.codeit.sb06deokhugamteam2.dashboard.dto.response.CursorPageResponsePopularReviewDto;
import com.codeit.sb06deokhugamteam2.dashboard.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DashboardService {
    private final DashboardRepository dashboardRepository;

    public CursorPageResponsePopularReviewDto findPopularReviews(PeriodType periodType, String direction,
                                                                 String cursor, String after, int limit) {
        Instant startDate = calculateStartDate(periodType);
        Instant endDate = calculateEndDate();

        Slice<PopularReviewDto> popularReviewDtoSlice = dashboardRepository.findPopularReviews(
                periodType, direction, cursor, after, limit, startDate, endDate
        );


        List<PopularReviewDto> popularReviewDtos = popularReviewDtoSlice.getContent();
        int lastIndex = popularReviewDtos.size() - 1;

        String nextCursor = Long.toString(popularReviewDtos.get(lastIndex).getRank());
        String nextAfter = popularReviewDtos.get(lastIndex).getCreatedAt().toString();

        long totalElements = dashboardRepository.countByRankingTypeAndPeriodTypeAndCreatedAtBetween(RankingType.REVIEW, periodType, startDate, endDate);

        CursorPageResponsePopularReviewDto cursorPageResponsePopularReviewDto = CursorPageResponsePopularReviewDto.builder()
                .nextCursor(nextCursor)
                .nextAfter(nextAfter)
                .size(popularReviewDtoSlice.getContent().size())
                .hasNext(popularReviewDtoSlice.hasNext())
                .content(popularReviewDtoSlice.getContent())
                .totalElements(totalElements)
                .build();

        return cursorPageResponsePopularReviewDto;
    }

    private Instant calculateStartDate(PeriodType periodType) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate startDate = switch (periodType) {
            case DAILY -> LocalDate.now().minusDays(1);
            case WEEKLY -> LocalDate.now().minusDays(7);
            case MONTHLY -> LocalDate.now().minusDays(30);
            case ALL_TIME -> LocalDate.of(1970, 1, 1);
        };

        return startDate.atStartOfDay(zoneId).toInstant();
    }

    private Instant calculateEndDate() {
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDate.now().minusDays(1).atTime(LocalTime.MAX).atZone(zoneId).toInstant();
    }
}