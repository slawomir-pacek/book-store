package org.example.bookstore;

import java.math.BigDecimal;
import org.example.bookstore.model.Book;
import org.example.bookstore.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {

    public static void main(final String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(final BookService bookService) {
        return args -> {
            Book book = new Book();

            book.setTitle("Clean Code");
            book.setAuthor("Robert C. Martin");
            book.setIsbn("9780132350884");
            book.setPrice(BigDecimal.valueOf(49.99));

            bookService.save(book);

            System.out.println("All books:");

            bookService.findAll()
                    .forEach(System.out::println);
        };
    }
}
