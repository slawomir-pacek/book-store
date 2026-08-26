package org.example.bookstore.mapper;

import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.dto.book.UpdateBookRequestDto;
import org.example.bookstore.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(Book book);

    Book toEntity(CreateBookRequestDto requestDto);

    void updateBookFromDto(UpdateBookRequestDto dto, @MappingTarget Book book);
}
