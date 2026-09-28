package org.example.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import org.example.bookstore.service.book.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDto bookDto;
    private Category category;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);

        bookDto = new BookDto();

        category = new Category();
        category.setId(1L);

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void findById_shouldReturnBookDto_whenBookExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.findById(1L);

        assertSame(bookDto, result);

        verify(bookRepository).findById(1L);
        verify(bookMapper).toDto(book);
    }

    @Test
    void findById_shouldThrowException_whenBookDoesNotExist() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(1L)
        );

        verify(bookRepository).findById(1L);
        verify(bookMapper, never()).toDto(any());
    }

    @Test
    void findAll_shouldReturnMappedBooks() {
        Book secondBook = new Book();
        secondBook.setId(2L);

        BookDto secondBookDto = new BookDto();

        Page<Book> bookPage = new PageImpl<>(List.of(book, secondBook), pageable,2);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(bookMapper.toDto(secondBook)).thenReturn(secondBookDto);

        Page<BookDto> result = bookService.findAll(pageable);

        assertEquals(2, result.getContent().size());
        assertSame(bookDto, result.getContent().get(0));
        assertSame(secondBookDto, result.getContent().get(1));

        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(book);
        verify(bookMapper).toDto(secondBook);
    }

    @Test
    void search_shouldReturnMappedBooks() {
        BookSearchParametersDto params =
                new BookSearchParametersDto(null, null, null);

        Specification<Book> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction();

        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable,1);

        when(bookSpecificationBuilder.build(params)).thenReturn(specification);
        when(bookRepository.findAll(specification, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.search(params, pageable);

        assertEquals(1, result.getContent().size());
        assertSame(bookDto, result.getContent().get(0));

        verify(bookSpecificationBuilder).build(params);
        verify(bookRepository).findAll(specification, pageable);
        verify(bookMapper).toDto(book);
    }

    @Test
    void save_shouldSaveBookWithCategories() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setCategoryIds(Set.of(1L));

        when(bookMapper.toEntity(requestDto)).thenReturn(book);
        when(categoryRepository.findAllById(requestDto.getCategoryIds())).
                thenReturn(List.of(category));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.save(requestDto);

        assertSame(bookDto, result);
        assertEquals(Set.of(category), book.getCategories());

        verify(bookMapper).toEntity(requestDto);
        verify(categoryRepository).findAllById(requestDto.getCategoryIds());
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @Test
    void update_shouldUpdateBookAndCategories() {
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto();
        requestDto.setCategoryIds(Set.of(1L));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(categoryRepository.findAllById(requestDto.getCategoryIds())).
                thenReturn(List.of(category));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.update(1L, requestDto);

        assertSame(bookDto, result);
        assertEquals(Set.of(category), book.getCategories());

        verify(bookRepository).findById(1L);
        verify(bookMapper).updateBookFromDto(requestDto, book);
        verify(categoryRepository).findAllById(requestDto.getCategoryIds());
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @Test
    void update_shouldThrowException_whenBookDoesNotExist() {
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto();

        when(bookRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> bookService.update(1L, requestDto)
        );

        verify(bookRepository).findById(1L);
        verify(bookMapper, never()).updateBookFromDto(any(), any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void deleteById_shouldDeleteBook_whenBookExists() {
        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        bookService.deleteById(1L);

        verify(bookRepository).findById(1L);
        verify(bookRepository).delete(book);
    }

    @Test
    void deleteById_shouldThrowException_whenBookDoesNotExist() {
        when(bookRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> bookService.deleteById(1L)
        );

        verify(bookRepository).findById(1L);
        verify(bookRepository, never()).delete(any(Book.class));
    }
}
