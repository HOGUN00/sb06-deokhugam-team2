package com.codeit.sb06deokhugamteam2.review.adapter.in;

import com.codeit.sb06deokhugamteam2.review.adapter.in.dto.CursorPageRequestReviewDto;
import com.codeit.sb06deokhugamteam2.review.adapter.in.dto.CursorPageResponseReviewDto;
import com.codeit.sb06deokhugamteam2.review.adapter.in.dto.ReviewCreateRequest;
import com.codeit.sb06deokhugamteam2.review.adapter.in.dto.ReviewDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;

@Tag(name = "리뷰 관리", description = "리뷰 관련 API")
public interface ReviewApi {

    @Operation(summary = "리뷰 등록", description = "새로운 리뷰를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "리뷰 등록 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 (입력값 검증 실패)")
    @ApiResponse(responseCode = "404", description = "도서 정보 없음")
    @ApiResponse(responseCode = "409", description = "이미 작성된 리뷰 존재")
    @ApiResponse(responseCode = "500", description = "서버 내부 요류")
    ResponseEntity<ReviewDto> postReview(@RequestBody(required = true) ReviewCreateRequest request);

    @Operation(summary = "리뷰 목록 조회", description = "검색 조건에 맞는 리뷰 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 목록 조회 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 (정렬 기준 오류, 페이지네이션 파라미터 오류, 요청자 ID 누락)")
    @ApiResponse(responseCode = "500", description = "서버 내부 요류")
    ResponseEntity<CursorPageResponseReviewDto> getReviews(
            @ParameterObject CursorPageRequestReviewDto request,
            @Parameter(
                    schema = @Schema(format = "uuid"),
                    in = ParameterIn.HEADER,
                    required = true,
                    name = "Deokhugam-Request-User-ID"
            ) String header
    );
}
