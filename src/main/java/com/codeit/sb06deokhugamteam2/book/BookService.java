package com.codeit.sb06deokhugamteam2.book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ReviewService reviewService;

    public void deleteSoft(UUID bookId) {

        reviewService.deleteSoftByBookId(UUID bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 도서입니다: {}", bookId));
        book.setDeletedAsTrue();
        bookRepository.save(book);
        log.info("도서 논리 삭제 완료: {}", bookId);
    }
}
