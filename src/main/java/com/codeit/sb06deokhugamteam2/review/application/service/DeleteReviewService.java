package com.codeit.sb06deokhugamteam2.review.application.service;

import com.codeit.sb06deokhugamteam2.review.application.port.in.DeleteReviewUseCase;
import com.codeit.sb06deokhugamteam2.review.application.port.in.command.DeleteReviewCommand;
import com.codeit.sb06deokhugamteam2.review.application.port.out.ReviewRepository;
import com.codeit.sb06deokhugamteam2.review.domain.ReviewDomain;
import com.codeit.sb06deokhugamteam2.review.domain.exception.ReviewNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteReviewService implements DeleteReviewUseCase {

    private final ReviewRepository reviewRepository;

    public DeleteReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional
    public void deleteReview(DeleteReviewCommand command) {
        ReviewDomain review = reviewRepository.findById(command.reviewId())
                .orElseThrow(() -> new ReviewNotFoundException(command.reviewId()));
        review.requireOwner(command.requestUserId());
        reviewRepository.delete(review);
    }
}
