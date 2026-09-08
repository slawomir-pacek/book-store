package org.example.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryDto(
        @NotBlank(message = "Id cannot be blank")
        Long id,
        @NotBlank(message = "Name cannot be blank")
        String name,
        String description
) {
}
