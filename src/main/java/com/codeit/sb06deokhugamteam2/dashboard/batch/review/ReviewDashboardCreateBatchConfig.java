package com.codeit.sb06deokhugamteam2.dashboard.batch.review;

import com.codeit.sb06deokhugamteam2.common.enums.PeriodType;
import com.codeit.sb06deokhugamteam2.common.enums.RankingType;
import com.codeit.sb06deokhugamteam2.dashboard.dto.data.ReviewReaderItemDto;
import com.codeit.sb06deokhugamteam2.dashboard.entity.Dashboard;
import com.codeit.sb06deokhugamteam2.dashboard.repository.DashboardRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
@RequiredArgsConstructor
public class ReviewDashboardCreateBatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final DashboardRepository dashboardRepository;

    //Job 정의
    @Bean
    public Job createPopularReviewJob() {
        return new JobBuilder("createPopularReviewJob", jobRepository)
                .start(createPopularReviewStep())
                .build();
    }

    //Step정의
    @Bean
    public Step createPopularReviewStep() {
        return new StepBuilder("createPopularReviewStep", jobRepository)
                .<ReviewReaderItemDto, Dashboard>chunk(100, transactionManager)
                .reader(createPopularReviewReader(null))
                .processor(createPopularReviewProcessor(null))
                .writer(createPopularReviewWriter())
                .build();
    }

    //읽기 로직 정의
    @Bean
    @StepScope
    public JpaPagingItemReader<ReviewReaderItemDto> createPopularReviewReader(
            @Value("#{jobParameters['periodType']}") PeriodType periodType
    ) {
        Instant startDate = calculateStartDate(periodType);
        Instant endDate = calculateEndDate();

        return new JpaPagingItemReaderBuilder<ReviewReaderItemDto>()
                .name("createPopularReviewReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("""
                        SELECT r.id, (COUNT(lk.id) * 0.3) + (COUNT(c.id) * 0.7) as score
                        FROM Review r
                        LEFT JOIN Comment c
                        ON c.review = r
                        WITH c.createdAT BETWEEN :startDate And :endDate
                        LEFT JOIN r.reviewLikes lk
                        WITH lk.likedAt as DATE BETWEEN :startDate And :endDate
                        GROUP BY r.id, u.id, b.id
                        ORDER BY (COUNT(lk.id) * 0.3) + (COUNT(c.id) * 0.7)
                        """)
                .parameterValues(Map.of("startDate", startDate, "endDate", endDate))
                .pageSize(100)
                .build();
    }

    //서비스 로직 정의
    @Bean
    @StepScope
    public ItemProcessor<ReviewReaderItemDto, Dashboard> createPopularReviewProcessor(
            @Value("#{jobParameters['periodType']}") PeriodType periodType
    ) {
        AtomicLong atomicLong =  new AtomicLong(1L);
        return popularReview ->
                Dashboard.builder()
                        .rank(atomicLong.getAndIncrement())
                        .score(popularReview.getScore())
                        .entityId(popularReview.getReviewId())
                        .periodType(periodType)
                        .rankingType(RankingType.REVIEW)
                        .build();
    }

    //저장 로직 정의
    @Bean
    public ItemWriter<Dashboard> createPopularReviewWriter() {
        return dashboardRepository::saveAll;
    }

    private Instant calculateStartDate(PeriodType periodType) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate startDate = switch (periodType){
            case DAILY -> LocalDate.now().minusDays(1);
            case WEEKLY -> LocalDate.now().minusDays(7);
            case MONTHLY -> LocalDate.now().minusDays(30);
            case ALL_TIME -> LocalDate.of(1970, 1, 1);
        };

        return startDate.atStartOfDay(zoneId).toInstant();
    }

    private Instant calculateEndDate() {
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDate.now().minusDays(1).atTime(LocalTime.MAX).atZone(zoneId).toInstant();
    }
}
