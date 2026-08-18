package org.example.bookstore.repository.book.spec;

import java.util.Arrays;
import org.example.bookstore.model.Book;
import org.example.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {

    private static final String TITLE_KEY = "title";

    @Override
    public String getKey() {
        return TITLE_KEY;
    }

    public Specification<Book> getSpecification(String[] params) {

        return (root, query, criteriaBuilder)
                -> root.get("title").in(Arrays.asList(params));
    }
}
