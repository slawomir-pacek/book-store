package org.example.bookstore.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.BookDto;
import org.example.bookstore.dto.BookSearchParametersDto;
import org.example.bookstore.dto.CreateBookRequestDto;
import org.example.bookstore.dto.UpdateBookRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.model.Book;
import org.example.bookstore.repository.book.BookRepository;
import org.example.bookstore.repository.book.BookSpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    @Transactional
    public BookDto update(Long id, UpdateBookRequestDto bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Book with id " + id + " not found"));

        Book updatedBook = bookRepository.save(book);

        return bookMapper.toDto(updatedBook);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Book with id " + id + " not found"));

        bookRepository.delete(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookDto> search(BookSearchParametersDto params, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification, pageable)
                .map(bookMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id " + id + " not found"));

        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toEntity(requestDto);
        Book savedBook = bookRepository.save(book);

        return bookMapper.toDto(savedBook);
    }
}
