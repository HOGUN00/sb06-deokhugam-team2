package com.codeit.sb06deokhugamteam2.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

public record CommentCreateRequest(
        @NotNull(message = "userId는 필수값입니다.") @UUID(message = "userId는 UUID 형식이어야 합니다.") String userId,
        @NotNull(message = "reviewId는 필수값입니다.") @UUID(message = "reviewId는 UUID 형식이어야 합니다.") String reviewId,
        @NotBlank(message = "content는 공백일수 없습니다.") String content
) {
}
