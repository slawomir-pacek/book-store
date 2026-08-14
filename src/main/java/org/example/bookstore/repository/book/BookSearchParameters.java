package org.example.bookstore.repository.book;

public record BookSearchParameters(
        String[] titles,
        String[] authors,
        String[] isbns
) {
}
