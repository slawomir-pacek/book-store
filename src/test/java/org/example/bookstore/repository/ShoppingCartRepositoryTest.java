package org.example.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.book.BookRepository;
import org.example.bookstore.repository.cart.CartItemRepository;
import org.example.bookstore.repository.cart.ShoppingCartRepository;
import org.example.bookstore.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUserId_shouldReturnShoppingCart() {
        User user = new User();
        user.setEmail("cart@test.com");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setShippingAddress("Test Street 1");

        user = userRepository.save(user);

        Book book = new Book();
        book.setTitle("Java Programming");
        book.setAuthor("John Smith");
        book.setIsbn("1234567890");
        book.setPrice(new BigDecimal("49.99"));
        book.setDeleted(false);

        book = bookRepository.save(book);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);

        shoppingCart = shoppingCartRepository.save(shoppingCart);

        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        shoppingCart.getCartItems().add(cartItem);

        cartItemRepository.save(cartItem);

        Optional<ShoppingCart> result =
                shoppingCartRepository.findByUserId(user.getId());

        assertThat(result).isPresent();
        ShoppingCart foundCart = result.get();

        assertThat(foundCart.getId())
                .isEqualTo(shoppingCart.getId());

        assertThat(foundCart.getUser().getId())
                .isEqualTo(user.getId());

        assertThat(foundCart.getCartItems())
                .hasSize(1);

        CartItem foundCartItem = foundCart.getCartItems()
                .iterator()
                .next();

        assertThat(foundCartItem.getQuantity())
                .isEqualTo(2);

        assertThat(foundCartItem.getBook().getId())
                .isEqualTo(book.getId());

        assertThat(foundCartItem.getBook().getTitle())
                .isEqualTo("Java Programming");
    }

    @Test
    void findByUserId_shouldReturnEmpty_whenCartDoesNotExist() {
        Optional<ShoppingCart> result =
                shoppingCartRepository.findByUserId(999L);

        assertThat(result).isEmpty();
    }
}
