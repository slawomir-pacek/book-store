package org.example.bookstore.repository.cart;

import java.util.Optional;
import org.example.bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByShoppingCartIdAndBookId(
            Long shoppingCartId,
            Long bookId
    );

    Optional<CartItem> findByIdAndShoppingCartUserId(
            Long cartItemId,
            Long userId
    );
}
