package org.example.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.bookstore.validation.FieldMatch;

@FieldMatch(
        first = "password",
        second = "repeatedPassword",
        message = "Passwords must match"
)
public record UserRegistrationRequestDto(
        @Email
        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "Password cannot be blank and should contains between 8 and 20 signs")
        @Size(min = 8, max = 20)
        String password,

        @NotBlank(message = "Repeated password cannot be blank")
        @Size(min = 8, max = 20)
        String repeatPassword,

        @NotBlank(message = "First name cannot be blank")
        String firstName,

        @NotBlank(message = "Last name cannot be blank")
        String lastName,

        String shippingAddress
) {
}
