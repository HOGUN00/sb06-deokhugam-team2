package com.codeit.sb06deokhugamteam2.dashboard.repository;

import com.codeit.sb06deokhugamteam2.book.entity.QBook;
import com.codeit.sb06deokhugamteam2.comment.entity.QComment;
import com.codeit.sb06deokhugamteam2.common.enums.PeriodType;
import com.codeit.sb06deokhugamteam2.common.enums.RankingType;
import com.codeit.sb06deokhugamteam2.dashboard.dto.data.PopularReviewDto;
import com.codeit.sb06deokhugamteam2.dashboard.entity.Dashboard;
import com.codeit.sb06deokhugamteam2.dashboard.entity.QDashboard;
import com.codeit.sb06deokhugamteam2.review.adapter.out.entity.QReview;
import com.codeit.sb06deokhugamteam2.review.adapter.out.entity.QReviewLike;
import com.codeit.sb06deokhugamteam2.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import java.time.Instant;
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

        if (cursor != null && after != null) {
            if (direction == Sort.Direction.ASC) {
                builder.and(
                        dashboard.rank.gt(Long.parseLong(cursor))
                                // 순위가 같을 경우
                                .or(dashboard.rank.eq(Long.parseLong(cursor)).and(dashboard.createdAt.gt(after)))
                );

            } else {
                builder.and(
                        dashboard.rank.lt(Long.parseLong(cursor))
                                .or(dashboard.rank.eq(Long.parseLong(cursor)).and(dashboard.createdAt.lt(after)))
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
    public Slice<PopularReviewDto> findPopularReviews(PeriodType periodType, String direction, String cursor,
                                                      String after, int limit, Instant startDate, Instant endDate) {

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
                        .on(review.id.eq(dashboard.entityId))
                        .leftJoin(comment)
                        .on(comment.review.eq(review).and(comment.createdAt.between(startDate, endDate)))
                        .leftJoin(review.likes, reviewLike)
                        .on(reviewLike.likedAt.between(startDate, endDate))
                        .leftJoin(review.book, book)
                        .leftJoin(review.user, user)
                        .where(dashboard.periodType.eq(periodType).and(dashboard.createdAt.between(startDate, endDate)))
                        .groupBy(dashboard.id, review.id, user.id, book.id)
                        .orderBy(primaryOrder, secondaryOrder)
                        .limit(limit + 1)
                        .fetch();

        boolean hasNext = popularReviewDtos.size() > limit;
        if (hasNext) {
            popularReviewDtos.remove(limit + 1);
        }

        return new SliceImpl<>(popularReviewDtos, Pageable.unpaged(), hasNext);
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
