package org.example.bookstore.service.category;

import java.util.List;
import org.example.bookstore.dto.book.BookDtoWithoutCategoryId;
import org.example.bookstore.dto.category.CategoryDto;

public interface CategoryService {

    List<CategoryDto> findAll();

    CategoryDto getById(Long id);

    CategoryDto save(CategoryDto categoryDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    List<BookDtoWithoutCategoryId> getBooksByCategoryId(Long categoryId);

    void deleteById(Long id);
}
