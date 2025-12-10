package com.codeit.sb06deokhugamteam2.dashboard.batch.user;

import com.codeit.sb06deokhugamteam2.common.enums.PeriodType;
import com.codeit.sb06deokhugamteam2.common.exception.ErrorCode;
import com.codeit.sb06deokhugamteam2.common.exception.exceptions.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class UserDashboardScheduler {


    private final JobLauncher jobLauncher;
    private final Job createRankingUsersJob;

    /**
     * 일간(DAILY) 사용자 랭킹 계산 Job 실행
     * 매일 00시 04분에 실행 (BookJob 스케줄과 시간 분리)
     */
    @Scheduled(cron = "0 4 0 * * ?")
    public void runDailyJob() {
        log.info("Starting Daily User Ranking Job");
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("periodType", PeriodType.DAILY.name())
                    .addLong("time", System.currentTimeMillis()) // 항상 새로운 파라미터 생성
                    .toJobParameters();

            jobLauncher.run(createRankingUsersJob, params);
        } catch (Exception e) {
            log.error("Failed to run daily user ranking job", e);
            throw new UserException(
                    ErrorCode.COMMON_EXCEPTION,
                    Map.of("message", "Failed to run daily user ranking job"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 주간(WEEKLY) 사용자 랭킹 계산 Job 실행
     * 매일 00시 05분에 실행
     */
    @Scheduled(cron = "0 5 0 * * ?")
    public void runWeeklyJob() {
        log.info("Starting Weekly User Ranking Job");
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("periodType", PeriodType.WEEKLY.name())
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(createRankingUsersJob, params);
        } catch (Exception e) {
            log.error("Failed to run weekly user ranking job", e);
            throw new UserException(
                    ErrorCode.COMMON_EXCEPTION,
                    Map.of("message", "Failed to run weekly user ranking job"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 월간(MONTHLY) 사용자 랭킹 계산 Job 실행
     * 매일 00시 06분에 실행
     */
    @Scheduled(cron = "0 6 0 * * ?")
    public void runMonthlyJob() {
        log.info("Starting Monthly User Ranking Job");
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("periodType", PeriodType.MONTHLY.name())
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(createRankingUsersJob, params);
        } catch (Exception e) {
            log.error("Failed to run monthly user ranking job", e);
            throw new UserException(
                    ErrorCode.COMMON_EXCEPTION,
                    Map.of("message", "Failed to run monthly user ranking job"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 전체 기간(ALL_TIME) 사용자 랭킹 계산 Job 실행
     * 매일 00시 07분에 실행
     */
    @Scheduled(cron = "0 7 0 * * ?")
    public void runEntireJob() {
        log.info("Starting All-Time User Ranking Job");
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("periodType", PeriodType.ALL_TIME.name())
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(createRankingUsersJob, params);
        } catch (Exception e) {
            log.error("Failed to run all-time user ranking job", e);
            throw new UserException(
                    ErrorCode.COMMON_EXCEPTION,
                    Map.of("message", "Failed to run entire user ranking job"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}