package org.example.bookstore.service.cart;

import org.example.bookstore.dto.cart.CartItemRequestDto;
import org.example.bookstore.dto.cart.CartItemResponseDto;
import org.example.bookstore.dto.cart.ShoppingCartResponseDto;
import org.example.bookstore.dto.cart.UpdateCartItemRequestDto;
import org.example.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart(User user);

    CartItemResponseDto addCartItem(User user, CartItemRequestDto request);

    CartItemResponseDto updateCartItem(
            User user,
            Long cartItemId,
            UpdateCartItemRequestDto request
    );

    void removeCartItem(User user, Long cartItemId);
}
