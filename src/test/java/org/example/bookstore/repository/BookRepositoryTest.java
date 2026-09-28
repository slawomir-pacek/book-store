package org.example.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Set;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Category;
import org.example.bookstore.repository.book.BookRepository;
import org.example.bookstore.repository.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findAllByCategoriesId_returnsBooksInRequestedCategory() {
        Category fantasy = category("Fantasy");
        Category history = category("History");

        Book fantasyBook = book("The Hobbit", "J.R.R. Tolkien", "isbn-001");
        fantasyBook.setCategories(Set.of(fantasy));
        bookRepository.save(fantasyBook);

        Book historyBook = book("History of Rome", "Author A", "isbn-002");
        historyBook.setCategories(Set.of(history));
        bookRepository.save(historyBook);

        Page<Book> result = bookRepository.findAllByCategoriesId(
                fantasy.getId(),
                PageRequest.of(0, 10)
        );

        assertThat(result.getContent())
                .extracting(Book::getTitle)
                .containsExactly("The Hobbit");
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findAllByCategoriesId_returnsEmptyPage_whenCategoryHasNoBooks() {
        Category category = category("Poetry");

        Page<Book> result = bookRepository.findAllByCategoriesId(
                category.getId(),
                PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    void findById_doesNotReturnSoftDeletedBook() {
        Book book = bookRepository.save(
                book("Deleted book", "Author B", "isbn-003")
        );

        bookRepository.deleteById(book.getId());
        bookRepository.flush();

        assertThat(bookRepository.findById(book.getId())).isEmpty();
    }

    private Category category(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    private Book book(String title, String author, String isbn) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPrice(new BigDecimal("29.99"));
        return book;
    }
}
