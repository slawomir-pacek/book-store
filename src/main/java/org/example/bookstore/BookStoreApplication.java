package org.example.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * Main application class for BookStore.
 */

@SpringBootApplication
public class BookStoreApplication {
    /**
     * Private constructor to prevent instantiation.
     */
    private BookStoreApplication() {
    }
    /**
     * Starts the Spring Boot application.
     *
     * @param args command line arguments
     */

    public static void main(final String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }
}
