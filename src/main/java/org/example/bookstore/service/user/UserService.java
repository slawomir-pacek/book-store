package org.example.bookstore.service.user;

import org.example.bookstore.dto.user.UserRegistrationRequestDto;
import org.example.bookstore.dto.user.UserResponseDto;
import org.example.bookstore.exception.RegistrationException;

public interface UserService {

    UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException;
}
