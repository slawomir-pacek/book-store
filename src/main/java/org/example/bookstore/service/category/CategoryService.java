package org.example.bookstore.service.category;

import org.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.dto.category.CreateCategoryRequestDto;
import org.example.bookstore.dto.category.UpdateCategoryRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryRequestDto categoryDto);

    CategoryDto update(Long id, UpdateCategoryRequestDto categoryDto);

    Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            Long categoryId, Pageable pageable);

    void deleteById(Long id);
}
