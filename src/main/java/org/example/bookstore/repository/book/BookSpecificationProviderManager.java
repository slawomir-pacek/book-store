package org.example.bookstore.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.model.Book;
import org.example.bookstore.repository.SpecificationProvider;
import org.example.bookstore.repository.SpecificationProviderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {

    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders
                .stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("No specification provider for key: " + key));
    }
}
