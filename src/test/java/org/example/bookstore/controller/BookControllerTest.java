package org.example.bookstore.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.BookSearchParametersDto;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.dto.book.UpdateBookRequestDto;
import org.example.bookstore.security.JwtUtil;
import org.example.bookstore.service.book.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private Pageable pageable;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        bookDto = new BookDto();
        bookDto.setId(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAll_shouldReturnBooks() throws Exception {
        // given
        Page<BookDto> expected = new PageImpl<>(List.of(bookDto), pageable,1);

        when(bookService.findAll(pageable)).thenReturn(expected);

        // when + then
        mockMvc.perform(get("/api/books")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));

        verify(bookService).findAll(pageable);
    }

    @Test
    @WithMockUser(roles = "USER")
    void search_shouldReturnBooks() throws Exception {
        // given
        Page<BookDto> expected = new PageImpl<>(
                List.of(bookDto),
                pageable,
                1
        );

        when(bookService.search(
                any(BookSearchParametersDto.class),
                eq(pageable)
        )).thenReturn(expected);

        // when + then
        mockMvc.perform(get("/api/books/search")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));

        verify(bookService).search(
                any(BookSearchParametersDto.class),
                eq(pageable)
        );
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBookById_shouldReturnBook() throws Exception {
        // given
        Long id = 1L;

        when(bookService.findById(id)).thenReturn(bookDto);

        // when + then
        mockMvc.perform(get("/api/books/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(bookService).findById(id);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBook_shouldReturnBook() throws Exception {
        // given
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setAuthor("Jam to napisał");
        request.setIsbn("123456789");
        request.setTitle("Chemia podstawy");
        request.setPrice(BigDecimal.valueOf(60));

        when(bookService.save(any(CreateBookRequestDto.class)))
                .thenReturn(bookDto);

        // when + then
        mockMvc.perform(post("/api/books")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(bookService).save(any(CreateBookRequestDto.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateBook_shouldReturnBook() throws Exception {
        // given
        Long id = 1L;

        UpdateBookRequestDto request = new UpdateBookRequestDto();

        when(bookService.update(
                eq(id),
                any(UpdateBookRequestDto.class)
        )).thenReturn(bookDto);

        // when + then
        mockMvc.perform(put("/api/books/{id}", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(bookService).update(
                eq(id),
                any(UpdateBookRequestDto.class)
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteBook_shouldCallService() throws Exception {
        // given
        Long id = 1L;

        // when + then
        mockMvc.perform(delete("/api/books/{id}", id))
                .andExpect(status().isOk());

        verify(bookService).deleteById(id);
    }
}
