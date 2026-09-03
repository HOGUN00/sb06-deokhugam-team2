package com.codeit.sb06deokhugamteam2.book.repository;

import com.codeit.sb06deokhugamteam2.book.entity.Book;
import com.codeit.sb06deokhugamteam2.book.fixture.BookFixture;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

//SQLRestriction이 버전 관리에 미치는 영향 테스트
//DataJpaTest가 h2 db로 자동대체하는 것을 막기위해 NONE 설정
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class BookVersionWithSQLRestrictionRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @PersistenceContext
    private EntityManager em;

    // Slice 테스트는 전체 로딩하지 않기 때문에 빈 주입 필요
    @TestConfiguration
    static class TestConfig {
        @PersistenceContext
        private EntityManager em;

        @Bean
        public JPAQueryFactory jpaQueryFactory() {
            return new JPAQueryFactory(em);
        }
    }

    @Test
    @DisplayName("책 저장 시 버전이 0으로 저장된다.")
    void testSQLRestrictionAndVersion() {
        // given
        Book book = BookFixture.createBook(1);
        bookRepository.save(book);

        // when
        Book savedBook = bookRepository.findById(book.getId()).orElseThrow();

        // then
        assertThat(savedBook.getVersion()).isEqualTo(0);
    }

    @Test
    @DisplayName("책 저장 후 수정 시 버전이 1로 증가한다.")
    void testVersionIncrementOnUpdate() {
        // given
        Book book = BookFixture.createBook(1);
        bookRepository.save(book);

        // when
        Book savedBook = bookRepository.findById(book.getId()).orElseThrow();
        book.updateDetails(
                "new title",
                "new author",
                "new description",
                "new publisher",
                LocalDate.now()
        );
        book.updateThumbnailUrl("new-thumbnail-url");

        bookRepository.save(savedBook);
        em.flush();
        em.clear();

        Book updatedBook = bookRepository.findById(book.getId()).orElseThrow();

        // then
        assertThat(updatedBook.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("JPQL 업데이트 시 버전 증가 시도를 하지 않는다.")
    void testVersionNotIncrementOnJPQLUpdate() {
        // given
        Book book = BookFixture.createBook(1);
        bookRepository.save(book);

        // when
        Book savedBook = bookRepository.findById(book.getId()).orElseThrow();

        bookRepository.deleteSoftById(savedBook.getId());

        // then
        assertThat(savedBook.getVersion()).isZero(); // 버전이 증가하지 않고 시도도 하지 않음
    }

    @Test
    @DisplayName("setter로 소프트 삭제, 더티체킹 시 버전이 1로 증가한다.")
    void testVersionIncrementOnSoftDeleteBySetter() {
        // given
        Book book = BookFixture.createBook(1);
        bookRepository.save(book);

        // when
        Book savedBook = bookRepository.findById(book.getId()).orElseThrow();
        savedBook.setDeleted(true);

        em.flush();

        // then
        assertThat(savedBook.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("소프트 삭제된 책은 조회되지 않는다.")
    void testSoftDeletedBookNotFound() {
        // given
        Book book = BookFixture.createBook(1);
        bookRepository.save(book);

        // when
        Book savedBook = bookRepository.findById(book.getId()).orElseThrow();
        bookRepository.deleteSoftById(savedBook.getId());
        em.flush();
        em.clear();

        // then
        boolean exists = bookRepository.findById(book.getId()).isPresent();
        assertThat(exists).isFalse();
    }
}
