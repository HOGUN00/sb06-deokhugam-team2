package com.codeit.sb06deokhugamteam2.review.application.port.in;

import com.codeit.sb06deokhugamteam2.review.application.port.in.command.DeleteReviewCommand;

public interface DeleteReviewUseCase {
    void deleteReview(DeleteReviewCommand command);
}
