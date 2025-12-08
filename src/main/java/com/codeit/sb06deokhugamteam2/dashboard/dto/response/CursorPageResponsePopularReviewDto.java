package com.codeit.sb06deokhugamteam2.dashboard.dto.response;

import com.codeit.sb06deokhugamteam2.dashboard.dto.data.PopularReviewDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CursorPageResponsePopularReviewDto {
    private List<PopularReviewDto> content;
    private String nextCursor;
    private String nextAfter;
    private int size;
    private long totalElements;
    private boolean hasNext;
}
