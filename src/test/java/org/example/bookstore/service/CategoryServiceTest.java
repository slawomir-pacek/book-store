package org.example.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.dto.category.CreateCategoryRequestDto;
import org.example.bookstore.dto.category.UpdateCategoryRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.mapper.CategoryMapper;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Category;
import org.example.bookstore.repository.book.BookRepository;
import org.example.bookstore.repository.category.CategoryRepository;
import org.example.bookstore.service.category.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Fantasy");
        category.setDescription("Fantasy books");

        categoryDto = new CategoryDto(
                1L,
                "Fantasy",
                "Fantasy books"
        );
    }

    @Test
    void findAll_shouldReturnMappedCategories() {
        Category secondCategory = new Category();
        secondCategory.setId(2L);

        CategoryDto secondCategoryDto = new CategoryDto(2L,"Science Fiction",
                "Science fiction books");

        Page<Category> categoryPage =
                new PageImpl<>(
                        List.of(category, secondCategory),
                        pageable,
                        2
                );

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);

        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        when(categoryMapper.toDto(secondCategory)).thenReturn(secondCategoryDto);

        Page<CategoryDto> result = categoryService.findAll(pageable);

        assertEquals(2, result.getContent().size());
        assertSame(categoryDto, result.getContent().get(0));
        assertSame(secondCategoryDto, result.getContent().get(1));

        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(category);
        verify(categoryMapper).toDto(secondCategory);
    }

    @Test
    void getById_shouldReturnMappedCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(1L);

        assertSame(categoryDto, result);

        verify(categoryRepository).findById(1L);
        verify(categoryMapper).toDto(category);
    }

    @Test
    void getById_shouldThrowExceptionWhenCategoryDoesNotExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> categoryService.getById(1L));

        assertEquals("Category with id 1 not found", exception.getMessage());

        verify(categoryRepository).findById(1L);
        verifyNoInteractions(categoryMapper);
    }

    @Test
    void save_shouldMapSaveAndReturnCategory() {
        CreateCategoryRequestDto requestDto =
                new CreateCategoryRequestDto("Fantasy","Fantasy books");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Fantasy");
        savedCategory.setDescription("Fantasy books");

        when(categoryMapper.toEntity(requestDto)).thenReturn(category);

        when(categoryRepository.save(category)).thenReturn(savedCategory);

        when(categoryMapper.toDto(savedCategory)).thenReturn(categoryDto);

        CategoryDto result = categoryService.save(requestDto);

        assertSame(categoryDto, result);

        verify(categoryMapper).toEntity(requestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(savedCategory);
    }

    @Test
    void update_shouldUpdateSaveAndReturnCategory() {
        UpdateCategoryRequestDto requestDto =
                new UpdateCategoryRequestDto("Updated Fantasy","Updated description");

        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("Updated Fantasy");
        updatedCategory.setDescription("Updated description");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        when(categoryRepository.save(category)).thenReturn(updatedCategory);

        when(categoryMapper.toDto(updatedCategory)).thenReturn(categoryDto);

        CategoryDto result = categoryService.update(1L, requestDto);

        assertSame(categoryDto, result);

        assertEquals("Updated Fantasy", category.getName());

        assertEquals("Updated description", category.getDescription());

        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(updatedCategory);
    }

    @Test
    void update_shouldThrowExceptionWhenCategoryDoesNotExist() {
        UpdateCategoryRequestDto requestDto =
                new UpdateCategoryRequestDto("Updated Fantasy","Updated description");

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> categoryService.update(1L, requestDto));

        assertEquals("Category with id 1 not found", exception.getMessage());

        verify(categoryRepository).findById(1L);
        verifyNoInteractions(categoryMapper);
    }

    @Test
    void deleteById_shouldDeleteCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteById(1L);

        verify(categoryRepository).findById(1L);
        verify(categoryRepository).delete(category);
    }

    @Test
    void deleteById_shouldThrowExceptionWhenCategoryDoesNotExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> categoryService.deleteById(1L));

        assertEquals("Category with id 1 not found", exception.getMessage());

        verify(categoryRepository).findById(1L);
        verifyNoInteractions(categoryMapper);
    }

    @Test
    void getBooksByCategoryId_shouldReturnMappedBooks() {
        Book book = new Book();
        book.setId(1L);

        Book secondBook = new Book();
        secondBook.setId(2L);

        BookDtoWithoutCategoryIds bookDto =
                new BookDtoWithoutCategoryIds(
                        1L,
                        "Book One",
                        "Author One",
                        "123456789",
                        BigDecimal.valueOf(39.99),
                        "Book description",
                        "image.jpg"
                );

        BookDtoWithoutCategoryIds secondBookDto =
                new BookDtoWithoutCategoryIds(
                        2L,
                        "Book Two",
                        "Author Two",
                        "987654321",
                        BigDecimal.valueOf(49.99),
                        "Second book description",
                        "image2.jpg"
                );

        Page<Book> bookPage = new PageImpl<>(List.of(book, secondBook), pageable,2);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        when(bookRepository.findAllByCategoriesId(1L, pageable)).thenReturn(bookPage);

        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDto);

        when(bookMapper.toDtoWithoutCategories(secondBook)).thenReturn(secondBookDto);

        Page<BookDtoWithoutCategoryIds> result =
                categoryService.getBooksByCategoryId(1L, pageable);

        assertEquals(2, result.getContent().size());
        assertSame(bookDto, result.getContent().get(0));
        assertSame(secondBookDto, result.getContent().get(1));

        verify(categoryRepository).findById(1L);
        verify(bookRepository).findAllByCategoriesId(1L, pageable);
        verify(bookMapper).toDtoWithoutCategories(book);
        verify(bookMapper).toDtoWithoutCategories(secondBook);
    }

    @Test
    void getBooksByCategoryId_shouldThrowExceptionWhenCategoryDoesNotExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getBooksByCategoryId(1L, pageable));

        assertEquals(
                "Category with id 1 not found", exception.getMessage());

        verify(categoryRepository).findById(1L);
        verifyNoInteractions(bookRepository);
        verifyNoInteractions(bookMapper);
    }
}
