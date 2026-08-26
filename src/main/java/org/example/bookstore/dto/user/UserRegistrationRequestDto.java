package org.example.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.bookstore.validation.FieldMatch;

@FieldMatch(
        first = "password",
        second = "repeatPassword",
        message = "Passwords must match"
)
public record UserRegistrationRequestDto(
        @Email
        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank
        @Size(min = 8, max = 20)
        String password,

        @NotBlank
        @Size(min = 8, max = 20)
        String repeatPassword,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        String shippingAddress
) {
}
