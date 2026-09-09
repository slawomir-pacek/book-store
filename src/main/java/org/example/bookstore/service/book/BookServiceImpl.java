package org.example.bookstore.service.book;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.BookSearchParametersDto;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.dto.book.UpdateBookRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Category;
import org.example.bookstore.repository.book.BookRepository;
import org.example.bookstore.repository.book.BookSpecificationBuilder;
import org.example.bookstore.repository.category.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public BookDto update(Long id, UpdateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Book with id " + id + " not found"));

        bookMapper.updateBookFromDto(requestDto, book);

        Set<Category> categories = new HashSet<>(
                categoryRepository.findAllById(requestDto.getCategoryIds())
        );

        book.setCategories(categories);

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

        Set<Category> categories = new HashSet<>(
                categoryRepository.findAllById(requestDto.getCategoryIds())
        );

        book.setCategories(categories);

        Book savedBook = bookRepository.save(book);

        return bookMapper.toDto(savedBook);
    }
}
