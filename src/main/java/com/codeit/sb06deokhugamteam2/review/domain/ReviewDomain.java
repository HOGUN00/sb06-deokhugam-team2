package com.codeit.sb06deokhugamteam2.review.domain;

import com.codeit.sb06deokhugamteam2.review.application.port.in.command.CreateReviewCommand;
import com.codeit.sb06deokhugamteam2.review.domain.exception.ReviewException;
import com.codeit.sb06deokhugamteam2.review.domain.exception.ReviewPermissionDeniedException;

import java.time.Instant;
import java.util.UUID;

public class ReviewDomain {

    private final UUID id;
    private final UUID bookId;
    private final UUID userId;
    private int rating;
    private String content;
    private final Instant createdAt;
    private Instant updatedAt;

    public ReviewDomain(
            UUID id,
            UUID bookId,
            UUID userId,
            Integer rating,
            String content,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = requiredId(id);
        this.bookId = requiredBookId(bookId);
        this.userId = requiredUserId(userId);
        this.rating = requiredRating(rating);
        this.content = requiredContent(content);
        this.createdAt = requiredCreatedAt(createdAt);
        this.updatedAt = requiredUpdatedAt(updatedAt);
    }

    private UUID requiredId(UUID id) {
        if (id == null) {
            throw new ReviewException("id is required");
        }
        return id;
    }

    private UUID requiredBookId(UUID bookId) {
        if (bookId == null) {
            throw new ReviewException("bookId is required");
        }
        return bookId;
    }

    private UUID requiredUserId(UUID userId) {
        if (userId == null) {
            throw new ReviewException("userId is required");
        }
        return userId;
    }

    private int requiredRating(Integer rating) {
        if (rating == null) {
            throw new ReviewException("rating is required");
        }
        if (rating < 1 || rating > 5) {
            throw new ReviewException("rating must be between 1 and 5");
        }
        return rating;
    }

    private String requiredContent(String content) {
        if (content == null || content.isBlank()) {
            throw new ReviewException("content is required");
        }
        return content;
    }

    private Instant requiredCreatedAt(Instant createdAt) {
        if (createdAt == null) {
            throw new ReviewException("createdAt is required");
        }
        return createdAt;
    }

    private Instant requiredUpdatedAt(Instant updatedAt) {
        if (updatedAt == null) {
            throw new ReviewException("updatedAt is required");
        }
        return updatedAt;
    }

    public static ReviewDomain create(CreateReviewCommand command) {
        UUID id = UUID.randomUUID();
        UUID bookId = command.bookId();
        UUID userId = command.userId();
        Integer rating = command.rating();
        String content = command.content();
        Instant createdAt = Instant.now();
        Instant updatedAt = createdAt;

        return new ReviewDomain(
                id,
                bookId,
                userId,
                rating,
                content,
                createdAt,
                updatedAt
        );
    }

    public static ReviewDomain loadSnapshot(Snapshot snapshot) {
        UUID id = snapshot.id();
        UUID bookId = snapshot.bookId();
        UUID userId = snapshot.userId();
        Integer rating = snapshot.rating();
        String content = snapshot.content();
        Instant createdAt = snapshot.createdAt();
        Instant updatedAt = snapshot.updatedAt();

        return new ReviewDomain(
                id,
                bookId,
                userId,
                rating,
                content,
                createdAt,
                updatedAt
        );
    }

    public Snapshot createSnapshot() {
        return new Snapshot(
                id,
                bookId,
                userId,
                rating,
                content,
                createdAt,
                updatedAt
        );
    }

    public UUID id() {
        return id;
    }

    public UUID bookId() {
        return bookId;
    }

    public UUID userId() {
        return userId;
    }

    public int rating() {
        return rating;
    }

    public void requireOwner(UUID requestUserId) {
        if (!userId.equals(requestUserId)) {
            throw new ReviewPermissionDeniedException(requestUserId);
        }
    }

    public record Snapshot(
            UUID id,
            UUID bookId,
            UUID userId,
            Integer rating,
            String content,
            Instant createdAt,
            Instant updatedAt
    ) {
    }
}
