package org.example.bookstore.service;

import java.util.List;
import org.example.bookstore.dto.BookDto;
import org.example.bookstore.dto.BookSearchParametersDto;
import org.example.bookstore.dto.CreateBookRequestDto;
import org.example.bookstore.dto.UpdateBookRequestDto;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;

public interface BookService {

    List<BookDto> findAll(SpringDataWebProperties.Pageable pageable);

    BookDto findById(Long id);

    BookDto save(CreateBookRequestDto requestDto);

    BookDto update(Long id, UpdateBookRequestDto bookDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto params);
}
