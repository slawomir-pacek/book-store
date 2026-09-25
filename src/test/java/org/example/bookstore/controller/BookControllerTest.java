package org.example.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.BookSearchParametersDto;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.dto.book.UpdateBookRequestDto;
import org.example.bookstore.service.book.BookService;
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

@ExtendWith(MockitoExtension.class)
class BookControllerTest {
    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private Pageable pageable;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        bookDto = new BookDto();
        bookDto.setId(1L);
    }

    @Test
    void getAll_shouldReturnBooks() {
        // given
        Page<BookDto> expected = new PageImpl<>(List.of(bookDto));

        when(bookService.findAll(pageable)).thenReturn(expected);

        // when
        Page<BookDto> actual = bookController.getAll(pageable);

        // then
        assertSame(expected, actual);
        verify(bookService).findAll(pageable);
    }

    @Test
    void search_shouldReturnBooks() {
        // given
        BookSearchParametersDto searchParameters = new BookSearchParametersDto(null, null, null);

        Page<BookDto> expected = new PageImpl<>(List.of(bookDto));

        when(bookService.search(searchParameters, pageable)).thenReturn(expected);

        // when
        Page<BookDto> actual = bookController.search(searchParameters, pageable);

        //then
        assertSame(expected, actual);
        verify(bookService).search(searchParameters, pageable);
    }

    @Test
    void getBookById_shouldReturnBook() {
        // given
        Long id = 1L;
        //BookDto bookDto = new BookDto(); - to już mam z kodu na górze
        //bookDto.setId(id); - to już mam z kodu na górze

        when(bookService.findById(id)).thenReturn(bookDto);

        // when
        BookDto actual = bookController.getBookById(id); // - prawdziwe wykonanie kodu

        // then
        assertSame(bookDto, actual);
        verify(bookService).findById(id);
    }

    @Test
    void createBook_shouldReturnsBook() {
        // given
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setAuthor("Jam to napisał");
        createBookRequestDto.setIsbn("123456789");
        createBookRequestDto.setTitle("Chemia podstawy");
        createBookRequestDto.setPrice(BigDecimal.valueOf(60));

        when(bookService.save(createBookRequestDto)).thenReturn(bookDto);
        //BookDto bookDto = new BookDto(); - to już mam z kodu na górze
        //bookDto.setId(id); - to już mam z kodu na górze

        // when
        BookDto actual = bookController.createBook(createBookRequestDto);

        // then
        assertSame(bookDto, actual);
        verify(bookService).save(createBookRequestDto);
    }

    @Test
    void updateBook_shouldReturnsBook() {
        // given
        Long id = 1L;
        UpdateBookRequestDto updateBookRequestDto = new UpdateBookRequestDto();

        when(bookService.update(id, updateBookRequestDto)).thenReturn(bookDto);

        // when
        BookDto actual = bookController.updateBook(id, updateBookRequestDto);

        // then
        assertSame(bookDto, actual);
        verify(bookService).update(id, updateBookRequestDto);
    }

    @Test
    void deleteBook_shouldCallService() {
        // given
        Long id = 1L;

        // when
        bookController.deleteBook(id);

        // then
        verify(bookService).deleteById(bookDto.getId());
    }
}
