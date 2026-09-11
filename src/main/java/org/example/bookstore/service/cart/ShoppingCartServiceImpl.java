package org.example.bookstore.service.cart;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.cart.CartItemRequestDto;
import org.example.bookstore.dto.cart.CartItemResponseDto;
import org.example.bookstore.dto.cart.ShoppingCartResponseDto;
import org.example.bookstore.dto.cart.UpdateCartItemRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.CartItemMapper;
import org.example.bookstore.mapper.ShoppingCartMapper;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.book.BookRepository;
import org.example.bookstore.repository.cart.CartItemRepository;
import org.example.bookstore.repository.cart.ShoppingCartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartResponseDto getShoppingCart(User user) {
        return shoppingCartMapper.toDto(getCartByUserId(user.getId()));
    }

    @Override
    @Transactional
    public CartItemResponseDto addCartItem(
            User user,
            CartItemRequestDto request
    ) {
        ShoppingCart shoppingCart = getCartByUserId(user.getId());

        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find book by id: " + request.bookId()
                ));

        CartItem cartItem = cartItemRepository
                .findByShoppingCartIdAndBookId(shoppingCart.getId(), book.getId())
                .orElseGet(() -> {
                    CartItem newCartItem = new CartItem();
                    newCartItem.setShoppingCart(shoppingCart);
                    newCartItem.setBook(book);
                    newCartItem.setQuantity(0);
                    return newCartItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + request.quantity());

        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    @Transactional
    public CartItemResponseDto updateCartItem(
            User user,
            Long cartItemId,
            UpdateCartItemRequestDto request
    ) {
        CartItem cartItem = getCartItemByIdAndUserId(cartItemId, user.getId());
        cartItem.setQuantity(request.quantity());

        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    @Transactional
    public void removeCartItem(User user, Long cartItemId) {
        CartItem cartItem = getCartItemByIdAndUserId(cartItemId, user.getId());
        cartItemRepository.delete(cartItem);
    }

    private ShoppingCart getCartByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart for user with id: " + userId
                ));
    }

    private CartItem getCartItemByIdAndUserId(Long cartItemId, Long userId) {
        return cartItemRepository.findByIdAndShoppingCartUserId(cartItemId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cart item by id: " + cartItemId
                ));
    }
}
