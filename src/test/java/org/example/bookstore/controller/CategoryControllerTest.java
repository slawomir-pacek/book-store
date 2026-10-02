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
import org.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.dto.category.CreateCategoryRequestDto;
import org.example.bookstore.dto.category.UpdateCategoryRequestDto;
import org.example.bookstore.security.JwtUtil;
import org.example.bookstore.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private Pageable pageable;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        categoryDto = new CategoryDto(
                1L,
                "Fantasy",
                "Fantasy books"
        );
    }

    @Test
    void createCategory_shouldReturnCategory() throws Exception {
        // given
        CreateCategoryRequestDto request =
                new CreateCategoryRequestDto(
                        "Fantasy",
                        "Fantasy books"
                );

        when(categoryService.save(any(CreateCategoryRequestDto.class)))
                .thenReturn(categoryDto);

        // when + then
        mockMvc.perform(post("/api/categories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Fantasy"))
                .andExpect(jsonPath("$.description")
                        .value("Fantasy books"));

        verify(categoryService)
                .save(any(CreateCategoryRequestDto.class));
    }

    @Test
    void getAll_shouldReturnCategories() throws Exception {
        // given
        Page<CategoryDto> expected = new PageImpl<>(
                List.of(categoryDto),
                pageable,
                1
        );

        when(categoryService.findAll(pageable))
                .thenReturn(expected);

        // when + then
        mockMvc.perform(get("/api/categories")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name")
                        .value("Fantasy"))
                .andExpect(jsonPath("$.content[0].description")
                        .value("Fantasy books"));

        verify(categoryService).findAll(pageable);
    }

    @Test
    void getCategoryById_shouldReturnCategory() throws Exception {
        // given
        Long id = 1L;

        when(categoryService.getById(id))
                .thenReturn(categoryDto);

        // when + then
        mockMvc.perform(get("/api/categories/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name")
                        .value("Fantasy"))
                .andExpect(jsonPath("$.description")
                        .value("Fantasy books"));

        verify(categoryService).getById(id);
    }

    @Test
    void updateCategory_shouldReturnCategory() throws Exception {
        // given
        Long categoryId = 1L;

        UpdateCategoryRequestDto request =
                new UpdateCategoryRequestDto(
                        "Updated Fantasy",
                        "Updated Fantasy books"
                );

        when(categoryService.update(
                eq(categoryId),
                any(UpdateCategoryRequestDto.class)
        )).thenReturn(categoryDto);

        // when + then
        mockMvc.perform(put("/api/categories/{id}", categoryId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name")
                        .value("Fantasy"))
                .andExpect(jsonPath("$.description")
                        .value("Fantasy books"));

        verify(categoryService).update(
                eq(categoryId),
                any(UpdateCategoryRequestDto.class)
        );
    }

    @Test
    void deleteCategory_shouldCallService() throws Exception {
        // given
        Long categoryId = 1L;

        // when + then
        mockMvc.perform(delete("/api/categories/{id}", categoryId))
                .andExpect(status().isNoContent());

        verify(categoryService)
                .deleteById(categoryId);
    }

    @Test
    void getBooksByCategoryId_shouldReturnBooks() throws Exception {
        // given
        Long categoryId = 1L;

        BookDtoWithoutCategoryIds bookDto =
                new BookDtoWithoutCategoryIds(
                        1L,
                        "Harry Potter",
                        "J.K. Rowling",
                        "32455432",
                        new BigDecimal("40.00"),
                        "Fantasy book",
                        "cover.jpg"
                );

        Page<BookDtoWithoutCategoryIds> expected =
                new PageImpl<>(
                        List.of(bookDto),
                        pageable,
                        1
                );

        when(categoryService.getBooksByCategoryId(
                categoryId,
                pageable
        )).thenReturn(expected);

        // when + then
        mockMvc.perform(get(
                        "/api/categories/{id}/books",
                        categoryId
                )
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title")
                        .value("Harry Potter"))
                .andExpect(jsonPath("$.content[0].author")
                        .value("J.K. Rowling"))
                .andExpect(jsonPath("$.content[0].isbn")
                        .value("32455432"));

        verify(categoryService)
                .getBooksByCategoryId(categoryId, pageable);
    }
}
