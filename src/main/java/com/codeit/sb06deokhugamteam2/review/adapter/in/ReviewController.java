package com.codeit.sb06deokhugamteam2.review.adapter.in;

import com.codeit.sb06deokhugamteam2.review.adapter.in.dto.CursorPageRequestReviewDto;
import com.codeit.sb06deokhugamteam2.review.adapter.in.dto.CursorPageResponseReviewDto;
import com.codeit.sb06deokhugamteam2.review.adapter.in.dto.ReviewCreateRequest;
import com.codeit.sb06deokhugamteam2.review.adapter.in.dto.ReviewDto;
import com.codeit.sb06deokhugamteam2.review.application.dto.ReviewDetail;
import com.codeit.sb06deokhugamteam2.review.application.dto.ReviewSummary;
import com.codeit.sb06deokhugamteam2.review.application.port.in.CreateReviewUseCase;
import com.codeit.sb06deokhugamteam2.review.application.port.in.DeleteReviewUseCase;
import com.codeit.sb06deokhugamteam2.review.application.port.in.GetReviewQuery;
import com.codeit.sb06deokhugamteam2.review.application.port.in.command.CreateReviewCommand;
import com.codeit.sb06deokhugamteam2.review.application.port.in.command.DeleteReviewCommand;
import com.codeit.sb06deokhugamteam2.review.application.port.in.query.ReviewPaginationQuery;
import com.codeit.sb06deokhugamteam2.review.application.port.in.query.ReviewQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/reviews")
public class ReviewController implements ReviewApi {

    private final ReviewApiMapper reviewMapper;
    private final CreateReviewUseCase createReviewUseCase;
    private final GetReviewQuery getReviewQuery;
    private final DeleteReviewUseCase deleteReviewUseCase;

    public ReviewController(
            ReviewApiMapper reviewMapper,
            CreateReviewUseCase createReviewUseCase,
            GetReviewQuery getReviewQuery,
            DeleteReviewUseCase deleteReviewUseCase
    ) {
        this.reviewMapper = reviewMapper;
        this.createReviewUseCase = createReviewUseCase;
        this.getReviewQuery = getReviewQuery;
        this.deleteReviewUseCase = deleteReviewUseCase;
    }

    @Override
    @PostMapping
    public ResponseEntity<ReviewDto> postReview(@RequestBody ReviewCreateRequest request) {
        CreateReviewCommand command = reviewMapper.toCreateReviewCommand(request);
        ReviewDetail detail = createReviewUseCase.createReview(command);
        ReviewDto response = reviewMapper.toReviewDetailResponse(detail);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<CursorPageResponseReviewDto> getReviews(
            @ModelAttribute CursorPageRequestReviewDto request,
            @RequestHeader(value = "Deokhugam-Request-User-ID") String header
    ) {
        ReviewPaginationQuery query = reviewMapper.toReviewPaginationQuery(request, header);
        ReviewSummary summary = getReviewQuery.readReviews(query);
        CursorPageResponseReviewDto response = reviewMapper.toReviewSummaryResponse(summary);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReview(
            @PathVariable(name = "reviewId") String request,
            @RequestHeader(value = "Deokhugam-Request-User-ID") String header
    ) {
        ReviewQuery query = reviewMapper.toReviewQuery(request, header);
        ReviewDetail detail = getReviewQuery.readReview(query);
        ReviewDto response = reviewMapper.toReviewDetailResponse(detail);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable(name = "reviewId") String request,
            @RequestHeader(value = "Deokhugam-Request-User-ID") String header
    ) {
        DeleteReviewCommand command = reviewMapper.toDeleteReviewCommand(request, header);
        deleteReviewUseCase.deleteReview(command);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
