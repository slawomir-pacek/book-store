package org.example.bookstore.repository.book;

public record BookSearchParametersDto(
        String[] titles,
        String[] authors,
        String[] isbns
) {
}
