package org.example.bookstore.service.user;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.RoleName;
import org.example.bookstore.dto.user.UserRegistrationRequestDto;
import org.example.bookstore.dto.user.UserResponseDto;
import org.example.bookstore.exception.RegistrationException;
import org.example.bookstore.mapper.UserMapper;
import org.example.bookstore.model.Role;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.RoleRepository;
import org.example.bookstore.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) {

        if (userRepository.findByEmail(request.email().toLowerCase()).isPresent()) {
            throw new RegistrationException(
                    "User with email " + request.email() + " already exists"
            );
        }

        User user = userMapper.toModel(request);

        user.setEmail(request.email().toLowerCase());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new RegistrationException(
                        "Role " + RoleName.USER + " doesn't exist"));

        user.setRoles(Set.of(userRole));

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }
}
