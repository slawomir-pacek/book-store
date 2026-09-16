package org.example.bookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import org.example.bookstore.Status;

public record UpdateOrderStatusRequestDto(
        @NotNull(message = "Status must not be null")
        Status status
) {
}
