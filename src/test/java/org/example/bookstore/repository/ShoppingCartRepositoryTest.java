package org.example.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.cart.ShoppingCartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private org.example.bookstore.repository.user.UserRepository userRepository;

    @Test
    void findByUserId_shouldReturnShoppingCart() {
        User user = new User();
        user.setEmail("cart@test.com");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setShippingAddress("Test Street 1");

        user = userRepository.save(user);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);

        shoppingCart = shoppingCartRepository.save(shoppingCart);

        Optional<ShoppingCart> result =
                shoppingCartRepository.findByUserId(user.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId())
                .isEqualTo(shoppingCart.getId());
        assertThat(result.get().getUser().getId())
                .isEqualTo(user.getId());
    }

    @Test
    void findByUserId_shouldReturnEmpty_whenCartDoesNotExist() {
        Optional<ShoppingCart> result =
                shoppingCartRepository.findByUserId(999L);

        assertThat(result).isEmpty();
    }
}
