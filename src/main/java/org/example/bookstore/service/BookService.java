package org.example.bookstore.service;

import java.util.List;
import org.example.bookstore.dto.BookDto;
import org.example.bookstore.dto.CreateBookRequestDto;

public interface BookService {

    List<BookDto> findAll();

    BookDto findById(Long id);

    BookDto save(CreateBookRequestDto requestDto);
}
