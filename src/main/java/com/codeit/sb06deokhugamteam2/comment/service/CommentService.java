package com.codeit.sb06deokhugamteam2.comment.service;

import com.codeit.sb06deokhugamteam2.comment.dto.CommentUpdateRequest;
import com.codeit.sb06deokhugamteam2.comment.entity.Comment;
import com.codeit.sb06deokhugamteam2.comment.dto.CommentCreateRequest;
import com.codeit.sb06deokhugamteam2.comment.dto.CommentDto;
import com.codeit.sb06deokhugamteam2.comment.mapper.CommentMapper;
import com.codeit.sb06deokhugamteam2.comment.repository.CommentRepository;
import com.codeit.sb06deokhugamteam2.common.exception.ErrorCode;
import com.codeit.sb06deokhugamteam2.common.exception.exceptions.CommentException;
import com.codeit.sb06deokhugamteam2.review.adapter.out.entity.Review;
import com.codeit.sb06deokhugamteam2.review.application.port.out.ReviewRepository;
import com.codeit.sb06deokhugamteam2.review.domain.exception.DuplicateReviewException;
import com.codeit.sb06deokhugamteam2.user.entity.User;
import com.codeit.sb06deokhugamteam2.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final  CommentMapper commentMapper;
    private final ReviewRepository reviewRepository;

    @PersistenceContext
    private EntityManager em;


    public CommentDto create(CommentCreateRequest request) {

        log.info("start CommentService.create(): userId = {},reviewId = {}",request.userId(),request.reviewId());

        UUID userId = UUID.fromString(request.userId());
        UUID reviewId = UUID.fromString(request.reviewId());


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommentException(ErrorCode.USER_NOT_FOUND, Map.of("userId", userId), HttpStatus.NOT_FOUND));

        Review review = em.getReference(Review.class, reviewId);


        Comment comment = Comment.builder()
                .user(user)
                .review(review)
                .content(request.content())
                .build();

        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toDto(savedComment);
    }

    public CommentDto update(UUID commentId, UUID userId, CommentUpdateRequest request) {

        log.info("updating comment : commentId = {},userId = {}",commentId,userId);

        Comment foundComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.INVALID_DATA, Map.of("commentId", commentId), HttpStatus.NOT_FOUND));

        if (!foundComment.getUser().getId().equals(userId)) {
            throw new CommentException(ErrorCode.INVALID_USER_DATA, Map.of("userId", userId), HttpStatus.FORBIDDEN);
        }

        foundComment.updateComment(request.content());

        Comment updatedComment = commentRepository.save(foundComment);

        log.info("Comment updated with id: {}", updatedComment.getId());

        return commentMapper.toDto(updatedComment);
    }

    public CommentDto readComment(UUID commentId) {
        log.info("reading comment : commentId = {}", commentId);

        Comment foundComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.INVALID_DATA, Map.of("commentId", commentId), HttpStatus.NOT_FOUND));


        return commentMapper.toDto(foundComment);
    }
}
