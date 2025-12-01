package com.codeit.sb06deokhugamteam2.review.domain.exception;

import java.util.UUID;

public class ReviewPermissionDeniedException extends ReviewException {

    public ReviewPermissionDeniedException(UUID requestUserId) {
        super("Permission denied: '%s'".formatted(requestUserId));
    }
}
