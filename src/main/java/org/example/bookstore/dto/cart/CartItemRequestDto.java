package org.example.bookstore.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemRequestDto(
        @NotNull
        Long bookId,

        @NotNull
        @Positive
        Integer quantity
) {
}
