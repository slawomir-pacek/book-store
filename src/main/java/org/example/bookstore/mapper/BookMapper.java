package org.example.bookstore.mapper;

import java.util.stream.Collectors;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.dto.book.UpdateBookRequestDto;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(Book book);

    Book toEntity(CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    void updateBookFromDto(UpdateBookRequestDto dto, @MappingTarget Book book);

    @AfterMapping
    default void setCategoryIds(
            @MappingTarget BookDto bookDto,
            Book book) {

        bookDto.setCategoryIds(
                book.getCategories()
                        .stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet())
        );
    }
}
