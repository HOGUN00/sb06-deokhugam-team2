package com.codeit.sb06deokhugamteam2.book;

import com.codeit.sb06deokhugamteam2.book.dto.request.BookUpdateRequest;
import com.codeit.sb06deokhugamteam2.book.entity.Book;
import com.codeit.sb06deokhugamteam2.book.repository.BookRepository;
import com.codeit.sb06deokhugamteam2.book.service.BookService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Import(BookConcurrencyVerificationTest.ProbeConfiguration.class)
class BookConcurrencyVerificationTest {

    private static final String TEST_ISBN_PREFIX = "concurrency-verification-";

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RepositoryFindProbeAspect repositoryFindProbeAspect;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanUp() {
        repositoryFindProbeAspect.clear();
        jdbcTemplate.update("DELETE FROM books WHERE isbn LIKE ?", TEST_ISBN_PREFIX + "%");
    }

    @Test
    @DisplayName("동시에 같은 도서를 수정하면 한 요청만 성공하고 다른 요청은 충돌한다")
    void concurrentUpdatesCauseConflict() throws Exception {
        Book book = createBook("update-conflict");
        int initialVersion = book.getVersion();
        repositoryFindProbeAspect.activate(new FindProbe(book.getId(), Map.of()));

        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<CallResult> results;
        try {
            Future<CallResult> updateA = executor.submit(() -> serviceUpdate(
                    "request-A", book.getId(), request("A", "title-A", "original-description")));
            Future<CallResult> updateB = executor.submit(() -> serviceUpdate(
                    "request-B", book.getId(), request("B", "original-title", "description-B")));
            results = List.of(
                    updateA.get(30, TimeUnit.SECONDS),
                    updateB.get(30, TimeUnit.SECONDS));
        } finally {
            repositoryFindProbeAspect.clear();
            shutdown(executor);
        }

        Book finalBook = bookRepository.findById(book.getId()).orElseThrow();
        assertOneSuccessAndOneConflict(results);
        assertThat(finalBook.getVersion()).isEqualTo(initialVersion + 1);
        assertThat(finalBook.getTitle() + "/" + finalBook.getDescription())
                .isIn("title-A/original-description", "original-title/description-B");
    }

    @Test
    @DisplayName("수정이 먼저 커밋되면 뒤늦은 논리 삭제는 충돌한다")
    void updateThenSoftDeleteCausesConflict() throws Exception {
        Book book = createBook("update-then-delete");
        repositoryFindProbeAspect.activate(new FindProbe(
                book.getId(), Map.of("delete-request", 250L)));

        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            Future<CallResult> update = executor.submit(() -> serviceUpdate(
                    "update-request", book.getId(),
                    request("update", "updated-title", "original-description")));
            Future<CallResult> delete = executor.submit(() ->
                    serviceSoftDelete("delete-request", book.getId()));

            CallResult updateResult = update.get(30, TimeUnit.SECONDS);
            CallResult deleteResult = delete.get(30, TimeUnit.SECONDS);
            repositoryFindProbeAspect.clear();

            Map<String, Object> row = jdbcTemplate.queryForMap(
                    "SELECT title, deleted, version FROM books WHERE id = ?", book.getId());
            assertThat(updateResult.success()).isTrue();
            assertOptimisticLockConflict(deleteResult);
            assertThat(row.get("deleted")).isEqualTo(false);
            assertThat(row.get("title")).isEqualTo("updated-title");
            assertThat(((Number) row.get("version")).intValue()).isEqualTo(book.getVersion() + 1);
        } finally {
            repositoryFindProbeAspect.clear();
            shutdown(executor);
        }
    }

    @Test
    @DisplayName("논리 삭제가 먼저 커밋되면 뒤늦은 수정은 충돌한다")
    void softDeleteThenUpdateCausesConflict() throws Exception {
        Book book = createBook("delete-then-update");
        repositoryFindProbeAspect.activate(new FindProbe(
                book.getId(), Map.of("update-request", 250L)));

        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            Future<CallResult> update = executor.submit(() -> serviceUpdate(
                    "update-request", book.getId(),
                    request("update", "updated-title", "original-description")));
            Future<CallResult> delete = executor.submit(() ->
                    serviceSoftDelete("delete-request", book.getId()));

            CallResult updateResult = update.get(30, TimeUnit.SECONDS);
            CallResult deleteResult = delete.get(30, TimeUnit.SECONDS);
            repositoryFindProbeAspect.clear();

            Map<String, Object> row = jdbcTemplate.queryForMap(
                    "SELECT deleted, version FROM books WHERE id = ?", book.getId());
            assertThat(deleteResult.success()).isTrue();
            assertOptimisticLockConflict(updateResult);
            assertThat(row.get("deleted")).isEqualTo(true);
            assertThat(((Number) row.get("version")).intValue()).isEqualTo(book.getVersion() + 1);
        } finally {
            repositoryFindProbeAspect.clear();
            shutdown(executor);
        }
    }

    private CallResult serviceUpdate(String threadName, UUID bookId, BookUpdateRequest request) {
        Thread.currentThread().setName(threadName);
        try {
            bookService.update(bookId, request, Optional.empty());
            return CallResult.succeeded();
        } catch (Throwable throwable) {
            return CallResult.failed(throwable);
        }
    }

    private CallResult serviceSoftDelete(String threadName, UUID bookId) {
        Thread.currentThread().setName(threadName);
        try {
            bookService.deleteSoft(bookId);
            return CallResult.succeeded();
        } catch (Throwable throwable) {
            return CallResult.failed(throwable);
        }
    }

    private Book createBook(String token) {
        Book book = Book.builder()
                .title("original-title")
                .author("original-author")
                .description("original-description")
                .publisher("original-publisher")
                .publishedDate(LocalDate.of(2020, 1, 1))
                .isbn(TEST_ISBN_PREFIX + token + "-" + UUID.randomUUID())
                .build();
        return bookRepository.saveAndFlush(book);
    }

    private static BookUpdateRequest request(String token, String title, String description) {
        BookUpdateRequest request = new BookUpdateRequest();
        request.setTitle(title);
        request.setAuthor("author-" + token);
        request.setDescription(description);
        request.setPublisher("publisher-" + token);
        request.setPublishedDate(LocalDate.of(2024, 1, 1));
        return request;
    }

    private static void assertOneSuccessAndOneConflict(List<CallResult> results) {
        assertThat(results).filteredOn(CallResult::success).hasSize(1);
        assertThat(results).filteredOn(result -> !result.success()).singleElement()
                .satisfies(BookConcurrencyVerificationTest::assertOptimisticLockConflict);
    }

    private static void assertOptimisticLockConflict(CallResult result) {
        assertThat(result.success()).isFalse();
        assertThat(result.error()).isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }

    private static void shutdown(ExecutorService executor) throws InterruptedException {
        executor.shutdownNow();
        assertThat(executor.awaitTermination(10, TimeUnit.SECONDS)).isTrue();
    }

    private record CallResult(boolean success, Throwable error) {
        private static CallResult succeeded() {
            return new CallResult(true, null);
        }

        private static CallResult failed(Throwable error) {
            return new CallResult(false, error);
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class ProbeConfiguration {
        @Bean
        RepositoryFindProbeAspect repositoryFindProbeAspect() {
            return new RepositoryFindProbeAspect();
        }
    }

    @Aspect
    static class RepositoryFindProbeAspect {
        private volatile FindProbe activeProbe;

        void activate(FindProbe probe) {
            this.activeProbe = probe;
        }

        void clear() {
            this.activeProbe = null;
        }

        @Around("bean(bookRepository) && execution(* *.findById(..))")
        Object synchronizeFindById(ProceedingJoinPoint joinPoint) throws Throwable {
            Object result = joinPoint.proceed();
            FindProbe probe = activeProbe;
            UUID bookId = (UUID) joinPoint.getArgs()[0];
            if (probe != null && probe.bookId().equals(bookId)) {
                probe.synchronize();
            }
            return result;
        }
    }

    private static final class FindProbe {
        private final UUID bookId;
        private final Map<String, Long> delayMillis;
        private final CyclicBarrier bothLoaded = new CyclicBarrier(2);

        private FindProbe(UUID bookId, Map<String, Long> delayMillis) {
            this.bookId = bookId;
            this.delayMillis = delayMillis;
        }

        private UUID bookId() {
            return bookId;
        }

        private void synchronize() {
            try {
                bothLoaded.await(10, TimeUnit.SECONDS);
                Long delay = delayMillis.get(Thread.currentThread().getName());
                if (delay != null) {
                    Thread.sleep(delay);
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("동시성 테스트 중 interrupt", exception);
            } catch (Exception exception) {
                throw new IllegalStateException("동시성 테스트 barrier 대기 실패", exception);
            }
        }
    }
}
