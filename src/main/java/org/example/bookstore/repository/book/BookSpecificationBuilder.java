package org.example.bookstore.repository.book;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.book.BookSearchParametersDto;
import org.example.bookstore.model.Book;
import org.example.bookstore.repository.SpecificationBuilder;
import org.example.bookstore.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book,
        BookSearchParametersDto> {
    private static final String TITLE_KEY = "title";
    private static final String AUTHOR_KEY = "author";
    private static final String ISBN_KEY = "isbn";

    private final SpecificationProviderManager<Book>
            bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.allOf();

        spec = addSpecificationIfPresent(
                TITLE_KEY,
                searchParameters.titles(),
                spec
        );

        spec = addSpecificationIfPresent(
                AUTHOR_KEY,
                searchParameters.authors(),
                spec
        );

        spec = addSpecificationIfPresent(
                ISBN_KEY,
                searchParameters.isbns(),
                spec
        );

        return spec;
    }

    private Specification<Book> addSpecificationIfPresent(
            String key,
            String[] params,
            Specification<Book> spec
    ) {
        if (params != null && params.length > 0) {
            spec = spec.and(
                    bookSpecificationProviderManager
                            .getSpecificationProvider(key)
                            .getSpecification(params)
            );
        }
        return spec;
    }
}
