package org.example.bookstore.service.user;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.user.UserRegistrationRequestDto;
import org.example.bookstore.dto.user.UserResponseDto;
import org.example.bookstore.exception.RegistrationException;
import org.example.bookstore.mapper.UserMapper;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) {

        String email = request.email().toLowerCase();

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RegistrationException(
                    "User with email " + request.email() + " already exists"
            );
        }

        User user = userMapper.toModel(request);

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }
}
