package com.codeit.sb06deokhugamteam2.dashboard.batch.book;

import com.codeit.sb06deokhugamteam2.common.enums.PeriodType;
import com.codeit.sb06deokhugamteam2.common.exception.ErrorCode;
import com.codeit.sb06deokhugamteam2.common.exception.exceptions.BookException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class BookDashboardScheduler {

    private static final ZoneId SEOUL_ZONE = ZoneId.of("Asia/Seoul");

    private final JobLauncher jobLauncher;
    private final Job createRankingBooksJob;

    @Scheduled(cron = "0 0 0 * * ?")
    public void runDailyJob() {
        runJob(PeriodType.DAILY, "Failed to run daily book ranking job");
    }

    @Scheduled(cron = "0 1 0 * * ?")
    public void runWeeklyJob() {
        runJob(PeriodType.WEEKLY, "Failed to run weekly book ranking job");
    }

    @Scheduled(cron = "0 2 0 * * ?")
    public void runMonthlyJob() {
        runJob(PeriodType.MONTHLY, "Failed to run monthly book ranking job");
    }

    @Scheduled(cron = "0 3 0 * * ?")
    public void runEntireJob() {
        runJob(PeriodType.ALL_TIME, "Failed to run entire book ranking job");
    }

    private void runJob(PeriodType periodType, String errorMessage) {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("periodType", periodType.name())
                    .addLocalDate("batchDate", LocalDate.now(SEOUL_ZONE))
                    .toJobParameters();

            jobLauncher.run(createRankingBooksJob, params);
        } catch (Exception e) {
            throw new BookException(
                    ErrorCode.COMMON_EXCEPTION,
                    Map.of("message", errorMessage),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
