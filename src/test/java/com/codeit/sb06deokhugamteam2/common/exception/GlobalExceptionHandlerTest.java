package com.codeit.sb06deokhugamteam2.common.exception;

import com.codeit.sb06deokhugamteam2.book.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new OptimisticLockingController())
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

    @Test
    @DisplayName("낙관적 락 충돌은 BOOK_STATE_CONFLICT 코드와 409 상태로 응답한다")
    void optimisticLockingFailureReturnsConflictResponse() throws Exception {
        mockMvc.perform(get("/test/optimistic-locking-failure"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.code").value("BOOK_STATE_CONFLICT"))
                .andExpect(jsonPath("$.message")
                        .value("다른 요청에서 도서가 수정되었거나 삭제되었습니다. 최신 상태를 확인해 주세요."))
                .andExpect(jsonPath("$.exceptionType")
                        .value(ObjectOptimisticLockingFailureException.class.getName()));
    }

    @RestController
    private static class OptimisticLockingController {

        @GetMapping("/test/optimistic-locking-failure")
        void fail() {
            throw new ObjectOptimisticLockingFailureException(Book.class, UUID.randomUUID());
        }
    }
}
