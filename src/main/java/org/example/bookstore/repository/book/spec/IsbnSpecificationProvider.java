package org.example.bookstore.repository.book.spec;

import java.util.Arrays;
import org.example.bookstore.model.Book;
import org.example.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {

    private static final String ISBN_KEY = "isbn";

    @Override
    public String getKey() {
        return "isbn";
    }

    public Specification<Book> getSpecification(String[] params) {

        return (root, query,
                criteriaBuilder) -> root.get("isbn").in(Arrays.asList(params));
    }
}
