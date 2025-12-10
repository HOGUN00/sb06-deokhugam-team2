package com.codeit.sb06deokhugamteam2.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentUpdateRequest(
        @NotBlank(message = "content는 공백일수 없습니다.") String content
) {

}
