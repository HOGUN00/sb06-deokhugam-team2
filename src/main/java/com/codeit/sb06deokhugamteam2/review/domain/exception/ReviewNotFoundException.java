package com.codeit.sb06deokhugamteam2.review.domain.exception;

import java.util.UUID;

public class ReviewNotFoundException extends ReviewException {

    public ReviewNotFoundException(UUID reviewId) {
        super("Review not found: '%s'".formatted(reviewId));
    }
}
