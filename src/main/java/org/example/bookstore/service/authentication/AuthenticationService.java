package org.example.bookstore.service.authentication;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.user.UserLoginRequestDto;
import org.example.bookstore.dto.user.UserLoginResponseDto;
import org.example.bookstore.exception.InvalidCredentialsException;
import org.example.bookstore.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserLoginResponseDto login(UserLoginRequestDto request) {

        String email = request.email().toLowerCase(Locale.ROOT);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            request.password()
                    )
            );

        } catch (DisabledException e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        String token = jwtUtil.generateToken(email);

        return new UserLoginResponseDto(token);
    }
}
