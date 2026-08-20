package org.example.bookstore.service;

import org.example.bookstore.dto.BookDto;
import org.example.bookstore.dto.BookSearchParametersDto;
import org.example.bookstore.dto.CreateBookRequestDto;
import org.example.bookstore.dto.UpdateBookRequestDto;
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
