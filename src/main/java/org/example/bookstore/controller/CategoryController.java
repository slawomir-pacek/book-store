package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.dto.category.CreateCategoryRequestDto;
import org.example.bookstore.dto.category.UpdateCategoryRequestDto;
import org.example.bookstore.service.category.CategoryService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Categories",
        description = "Endpoints for managing book categories"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Create a new category",
            description = "Creates a new book category"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(
            @Valid @RequestBody CreateCategoryRequestDto createCategoryRequestDto) {
        return categoryService.save(createCategoryRequestDto);
    }

    @Operation(
            summary = "Get all categories",
            description = "Returns a list of all available book categories"
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Page<CategoryDto> getAll(@ParameterObject Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @Operation(
            summary = "Get category by ID",
            description = "Returns a book category with the specified ID"
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Operation(
            summary = "Update a category",
            description = "Updates an existing book category with the specified ID"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CategoryDto updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequestDto updateCategoryRequestDto) {
        return categoryService.update(id, updateCategoryRequestDto);
    }

    @Operation(
            summary = "Delete a category",
            description = "Deletes the book category with the specified ID"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @Operation(
            summary = "Get books by category",
            description = "Returns all books assigned to the category with the specified ID"
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/books")
    public Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            @PathVariable Long id,
            @ParameterObject Pageable pageable) {

        return categoryService.getBooksByCategoryId(id, pageable);
    }
}
