package com.codeit.sb06deokhugamteam2.dashboard.batch.review;

import com.codeit.sb06deokhugamteam2.common.enums.PeriodType;
import com.codeit.sb06deokhugamteam2.review.domain.exception.ReviewException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewDashboardScheduler {
    private final Job createPopularReviewJob;
    private final JobLauncher jobLauncher;

    //초 분 시 일 월 요일
    @Scheduled(cron = "0 0 0 * * ?")
    public void runDailyJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("periodType", PeriodType.DAILY.name())
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(createPopularReviewJob, params);
        } catch (Exception e) {
            throw new ReviewException("인기 리뷰 Daily Scheduler 실행 실패");
        }
    }

    @Scheduled(cron = "0 1 0 * * ?")
    public void runWeeklyJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("periodType", PeriodType.WEEKLY.name())
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(createPopularReviewJob, params);
        } catch(Exception e) {
            throw new ReviewException("인기 리뷰 Weekly Scheduler 실행 실패");
        }
    }

    @Scheduled(cron = "0 2 0 * * ?")
    public void runMonthlyJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("periodType", PeriodType.MONTHLY.name())
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(createPopularReviewJob, params);
        } catch (Exception e) {
            throw new ReviewException("인기 리뷰 Monthly Scheduler 실행 실패");
        }
    }

    @Scheduled(cron = "0 3 0 * * ?")
    public void runEntireJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("periodType", PeriodType.MONTHLY.name())
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(createPopularReviewJob, params);
        } catch (Exception e) {
            throw new ReviewException("인기 리뷰 Entire Scheduler 실행 실패");
        }
    }
}
