package org.example.bookstore.mapper;

import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);
}
