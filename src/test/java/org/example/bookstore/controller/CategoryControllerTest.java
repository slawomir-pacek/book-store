package org.example.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import org.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.dto.category.CreateCategoryRequestDto;
import org.example.bookstore.dto.category.UpdateCategoryRequestDto;
import org.example.bookstore.service.category.CategoryService;
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
public class CategoryControllerTest {
    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private Pageable pageable; //zmianna dla wszystkich testów
    private CategoryDto categoryDto; //zmienna dla wszystkich testów

    @BeforeEach
    public void setUp() {
        pageable = PageRequest.of(0, 10);
        categoryDto = new CategoryDto(1L, "Fantasy", "Fantasy books");
    }

    @Test
    public void createCategory_shouldReturnCategory() {
        // given
        CreateCategoryRequestDto createCategoryRequestDto =
                new CreateCategoryRequestDto("Fantasy", "Fantasy books");

        when(categoryService.save(createCategoryRequestDto)).thenReturn(categoryDto);

        // when
        CategoryDto actual = categoryController.createCategory(createCategoryRequestDto);

        // then
        assertSame(categoryDto, actual);
        verify(categoryService).save(createCategoryRequestDto);
    }

    @Test
    public void getAll_shouldReturnCategories() {
        // given
        Page<CategoryDto> expected = new PageImpl<>(List.of(categoryDto));

        when(categoryService.findAll(pageable)).thenReturn(expected);

        // when
        Page<CategoryDto> actual = categoryController.getAll(pageable);

        // then
        assertSame(expected, actual);
        verify(categoryService).findAll(pageable);
    }

    @Test
    public void getCategoryById_shouldReturnCategory() {
        // given
        Long id = 1L;

        when(categoryService.getById(id)).thenReturn(categoryDto);

        // when
        CategoryDto actual = categoryController.getCategoryById(id);

        // then
        assertSame(categoryDto, actual);
        verify(categoryService).getById(id);
    }

    @Test
    void updateCategory_shouldReturnCategory() {
        // given
        Long categoryId = 1L;
        UpdateCategoryRequestDto request =
                new UpdateCategoryRequestDto("Updated Fantasy", "Updated Fantasy books");

        when(categoryService.update(categoryId, request))
                .thenReturn(categoryDto);

        // when
        CategoryDto actual =
                categoryController.updateCategory(categoryId, request);

        // then
        assertSame(categoryDto, actual);
        verify(categoryService).update(categoryId, request);
    }

    @Test
    void deleteCategory_shouldCallService() {
        // given
        Long categoryId = 1L;

        // when
        categoryController.deleteCategory(categoryId);

        // then
        verify(categoryService).deleteById(categoryId);
    }

    @Test
    void getBooksByCategoryId_shouldReturnBooks() {
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
                new PageImpl<>(List.of(bookDto));

        when(categoryService.getBooksByCategoryId(categoryId, pageable))
                .thenReturn(expected);

        // when
        Page<BookDtoWithoutCategoryIds> actual =
                categoryController.getBooksByCategoryId(categoryId, pageable);

        // then
        assertSame(expected, actual);
        verify(categoryService)
                .getBooksByCategoryId(categoryId, pageable);
    }
}
