package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.cart.CartItemRequestDto;
import org.example.bookstore.dto.cart.CartItemResponseDto;
import org.example.bookstore.dto.cart.ShoppingCartResponseDto;
import org.example.bookstore.dto.cart.UpdateCartItemRequestDto;
import org.example.bookstore.model.User;
import org.example.bookstore.service.cart.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart", description = "Endpoints for managing shopping carts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get the current user's shopping cart")
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ShoppingCartResponseDto getShoppingCart(
            @AuthenticationPrincipal User user
    ) {
        return shoppingCartService.getShoppingCart(user);
    }

    @Operation(summary = "Add a book to the shopping cart")
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartItemResponseDto addCartItem(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CartItemRequestDto request
    ) {
        return shoppingCartService.addCartItem(user, request);
    }

    @Operation(summary = "Update a shopping-cart item quantity")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/cart-items/{cartItemId}")
    public CartItemResponseDto updateCartItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemRequestDto request
    ) {
        return shoppingCartService.updateCartItem(user, cartItemId, request);
    }

    @Operation(summary = "Remove an item from the shopping cart")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/cart-items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCartItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId
    ) {
        shoppingCartService.removeCartItem(user, cartItemId);
    }
}
