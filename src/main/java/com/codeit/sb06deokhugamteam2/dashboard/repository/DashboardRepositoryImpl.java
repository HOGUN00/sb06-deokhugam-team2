package com.codeit.sb06deokhugamteam2.dashboard.repository;

import com.codeit.sb06deokhugamteam2.book.entity.QBook;
import com.codeit.sb06deokhugamteam2.comment.entity.QComment;
import com.codeit.sb06deokhugamteam2.common.enums.PeriodType;
import com.codeit.sb06deokhugamteam2.common.enums.RankingType;
import com.codeit.sb06deokhugamteam2.dashboard.dto.data.PopularReviewDto;
import com.codeit.sb06deokhugamteam2.dashboard.entity.Dashboard;
import com.codeit.sb06deokhugamteam2.dashboard.entity.QDashboard;
import com.codeit.sb06deokhugamteam2.review.adapter.out.entity.QReview;
import com.codeit.sb06deokhugamteam2.like.adapter.out.entity.QReviewLike;
import com.codeit.sb06deokhugamteam2.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@RequiredArgsConstructor
public class DashboardRepositoryImpl implements DashboardRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QDashboard dashboard = QDashboard.dashboard;
    private final QReview review = QReview.review;
    private final QUser user = QUser.user;
    private final QBook book = QBook.book;
    private final QComment comment = QComment.comment;
    private final QReviewLike reviewLike = QReviewLike.reviewLike;

    @Override
    public List<Dashboard> findPopularBookListByCursor(
            RankingType rankingType,
            PeriodType period,
            String cursor,
            Instant after,
            Sort.Direction direction,
            Integer limit
    ) {
        QDashboard dashboard = QDashboard.dashboard;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(dashboard.rankingType.eq(rankingType));
        builder.and(dashboard.periodType.eq(period));

        /*
        오늘 생성된 대시보드부터 조회
        createdAt이 Instant이라서 스케쥴 실행시간인 KST 00:00을 전날 UTC 15:00로 변환
         */
        Instant today = LocalDate.now()
                .atStartOfDay(ZoneId.of("Asia/Seoul"))
                .toInstant();

        builder.and(dashboard.createdAt.goe(today));

        if (cursor != null && after != null) {
            if (direction == Sort.Direction.ASC) {
                builder.and(
                        dashboard.rank.gt(Long.parseLong(cursor))
                );

            } else {
                builder.and(
                        dashboard.rank.lt(Long.parseLong(cursor))
                );
            }
        }

        return queryFactory
                .selectFrom(dashboard)
                .where(builder)
                .orderBy(direction == Sort.Direction.ASC ?
                                dashboard.rank.asc() :      // 1등부터
                                dashboard.rank.desc(),      // 꼴등부터
                        direction == Sort.Direction.ASC ?
                                dashboard.createdAt.asc() :      // 오래된 순
                                dashboard.createdAt.desc()       // 최신 순
                ).limit(limit + 1)
                .fetch();
    }

    @Override
    public Slice<PopularReviewDto> findPopularReviews(PeriodType periodType, String direction, Long cursor,
                                                      Instant after, int limit, Instant startDate, Instant endDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant batchStartDate = LocalDate.now().atStartOfDay(zoneId).toInstant();
        Instant batchEndDate = LocalDate.now().atTime(LocalTime.MAX).atZone(zoneId).toInstant();

        OrderSpecifier<?> primaryOrder = getPrimaryOrder(direction);
        OrderSpecifier<?> secondaryOrder = getSecondaryOrder(direction);

        List<PopularReviewDto> popularReviewDtos =
                queryFactory.select(Projections.constructor(PopularReviewDto.class,
                                dashboard.id, review.id, book.id, book.title,
                                book.thumbnailUrl, user.id, user.nickname,
                                review.content, review.rating, dashboard.periodType,
                                dashboard.createdAt, dashboard.rank, dashboard.score,
                                reviewLike.id.count(), comment.id.count()
                        ))
                        .from(dashboard)
                        .innerJoin(review)
                        .on(review.id.eq(dashboard.entityId).and(review.deleted.isFalse()))
                        .leftJoin(comment)
                        .on(comment.review.eq(review).and(comment.createdAt.between(startDate, endDate)))
                        .leftJoin(reviewLike)
                        .on(reviewLike.review.eq(review).and(reviewLike.likedAt.between(startDate, endDate)))
                        .innerJoin(review.book, book)
                        .on(book.deleted.isFalse())
                        .innerJoin(review.user, user)
                        .on(user.deletedAt.isNull())
                        .where(dashboard.periodType.eq(periodType).and(dashboard.createdAt.between(batchStartDate, batchEndDate)),
                                getCursorAndAfterCondition(direction, cursor, after))
                        .groupBy(dashboard.id, review.id, user.id, book.id)
                        .orderBy(primaryOrder, secondaryOrder)
                        .limit(limit + 1)
                        .fetch();

        boolean hasNext = popularReviewDtos.size() > limit;
        if (hasNext) {
            popularReviewDtos.remove(popularReviewDtos.size() - 1);
        }

        return new SliceImpl<>(popularReviewDtos, Pageable.unpaged(), hasNext);
    }

    private BooleanExpression getCursorAndAfterCondition(String direction, Long cursor, Instant after) {
        if (cursor == null || after == null) {
            return null;
        }

        if (direction.equalsIgnoreCase("ASC")) {
            return dashboard.rank.gt(cursor)
                    .or(dashboard.rank.eq(cursor).and(dashboard.createdAt.after(after)));
        } else {
            return dashboard.rank.lt(cursor)
                    .or(dashboard.rank.eq(cursor).and(dashboard.createdAt.before(after)));
        }
    }

    private OrderSpecifier<?> getPrimaryOrder(String direction) {
        if (direction.equalsIgnoreCase("ASC")) {
            return dashboard.rank.asc();
        }
        return dashboard.rank.desc();
    }

    private OrderSpecifier<?> getSecondaryOrder(String direction) {
        if (direction.equalsIgnoreCase("ASC")) {
            return dashboard.createdAt.asc();
        }
        return dashboard.createdAt.desc();
    }
}
