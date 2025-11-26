package com.codeit.sb06deokhugamteam2.book.controller;

import com.codeit.sb06deokhugamteam2.book.dto.request.BookCreateRequest;
import com.codeit.sb06deokhugamteam2.book.dto.data.BookDto;
import com.codeit.sb06deokhugamteam2.book.dto.request.BookImageCreateRequest;
import com.codeit.sb06deokhugamteam2.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BookDto> create(
            @RequestPart(value = "bookData") @Valid BookCreateRequest bookCreateRequest,
            @RequestPart(value = "thumbnailImage", required = false) MultipartFile imageData
    ) {
        Optional<BookImageCreateRequest> bookImageCreateRequest = resolveBookImageCreateRequest(imageData);
        BookDto bookDto = bookService.create(bookCreateRequest, bookImageCreateRequest);

        return ResponseEntity.ok(bookDto);
    }

    private Optional<BookImageCreateRequest> resolveBookImageCreateRequest(MultipartFile imageData) {
        if (imageData.isEmpty()) {
            return Optional.empty();
        } else {
            try {
                BookImageCreateRequest bookImageCreateRequest = new BookImageCreateRequest(
                        imageData.getBytes(),
                        imageData.getContentType()
                );

                return Optional.of(bookImageCreateRequest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
