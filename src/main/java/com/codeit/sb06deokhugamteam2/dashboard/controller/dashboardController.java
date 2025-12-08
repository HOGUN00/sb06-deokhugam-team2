package com.codeit.sb06deokhugamteam2.dashboard.controller;

import com.codeit.sb06deokhugamteam2.common.enums.PeriodType;
import com.codeit.sb06deokhugamteam2.dashboard.dto.response.CursorPageResponsePopularReviewDto;
import com.codeit.sb06deokhugamteam2.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
@RequiredArgsConstructor
public class dashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/reviews/popular")
    public ResponseEntity<CursorPageResponsePopularReviewDto> popularReviews(
            @RequestParam(value = "period", defaultValue = "DAILY") PeriodType periodType,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction,
            @RequestParam(value = "cursor", required = false) String cursor,
            @RequestParam(value = "after", required = false) String after,
            @RequestParam(value = "limit", defaultValue = "50") int limit
            ) {
        CursorPageResponsePopularReviewDto cursorPageResponsePopularReviewDto
                = dashboardService.findPopularReviews(
            periodType, direction, cursor, after, limit
        );

        return ResponseEntity.ok(cursorPageResponsePopularReviewDto);
    }
}
