package org.example.bookstore.repository.book.spec;

import java.util.Arrays;
import org.example.bookstore.model.Book;
import org.example.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {

    private static final String AUTHOR_KEY = "author";

    @Override
    public String getKey() {
        return "author";
    }

    public Specification<Book> getSpecification(String[] params) {

        return (root, query, criteriaBuilder)
                -> root.get("author").in(Arrays.asList(params));
    }
}
