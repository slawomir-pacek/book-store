package org.example.bookstore.service.book;

import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.BookSearchParametersDto;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.dto.book.UpdateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    Page<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto save(CreateBookRequestDto requestDto);

    BookDto update(Long id, UpdateBookRequestDto bookDto);

    void deleteById(Long id);

    Page<BookDto> search(BookSearchParametersDto params, Pageable pageable);
}
