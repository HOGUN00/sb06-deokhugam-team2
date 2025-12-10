package com.codeit.sb06deokhugamteam2.dashboard.batch.user;

import com.codeit.sb06deokhugamteam2.user.dto.PowerUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.UUID;


@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class UserDashboardJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    // 사용자 랭킹 계산 Job 정의
    @Bean
    public Job createRankingUsersJob(Step calculateUserRankingStep) {
        log.info("Configuring createRankingUsersJob");
        return new JobBuilder("createRankingUsersJob", jobRepository)
                .start(calculateUserRankingStep)
                .build();
    }

    // 사용자 랭킹 계산 Step 정의
    @Bean
    @JobScope // Job 파라미터를 사용하기 위해 Step에 Scope 지정
    public Step calculateUserRankingStep(
            ItemReader<PowerUserDto> userRankingReader,
            ItemProcessor<PowerUserDto, PowerUserDto> userRankingProcessor,
            ItemWriter<PowerUserDto> userRankingWriter) {

        log.info("Configuring calculateUserRankingStep");
        return new StepBuilder("calculateUserRankingStep", jobRepository)
                .<PowerUserDto, PowerUserDto>chunk(100, transactionManager)
                .reader(userRankingReader)
                .processor(userRankingProcessor)
                .writer(userRankingWriter)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<PowerUserDto> userRankingReader(
            @Value("#{jobParameters['periodType']}") String periodType) {

        log.info("User Ranking Reader initialized for period: {}", periodType);

        // 더미 Reader를 반환합니다.
        return new ItemReader<>() {
            private final List<PowerUserDto> dummyData = List.of(
                    new PowerUserDto(UUID.randomUUID(), "DummyUser", periodType, null, 0, 100.0, 50.0, 10L, 5L)
            );
            private int index = 0;

            @Override
            public PowerUserDto read() {
                // 더미 데이터 1건만 읽고, 다음 호출에서 null을 반환하여 Step을 종료시킵니다.
                return (index < dummyData.size()) ? dummyData.get(index++) : null;
            }
        };
    }

    //ItemProcessor 정의 (데이터를 그대로 통과시키며 로그만 남김)
    @Bean
    @StepScope
    public ItemProcessor<PowerUserDto, PowerUserDto> userRankingProcessor() {
        return item -> {
            log.debug("Processing dummy item: {}", item.getNickname());
            return item;
        };
    }

    //ItemWriter 정의 (DB에 저장하지 않고 로그만 남김)
    @Bean
    @StepScope
    public ItemWriter<PowerUserDto> userRankingWriter() {
        return items -> {
            log.info("Writing {} dummy items. (DB Save Skipped)", items.size());
        };
    }
}