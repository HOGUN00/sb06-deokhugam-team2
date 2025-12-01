package com.codeit.sb06deokhugamteam2.review.application.port.in.command;

import java.util.UUID;

public record DeleteReviewCommand(UUID reviewId, UUID requestUserId) {
}
