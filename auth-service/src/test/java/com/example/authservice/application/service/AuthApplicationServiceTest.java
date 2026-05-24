package com.example.authservice.application.service;

import com.example.authservice.application.dto.LoginRequest;
import com.example.authservice.application.dto.RegisterRequest;
import com.example.authservice.domain.exception.InvalidCredentialsException;
import com.example.authservice.domain.exception.UserAlreadyExistsException;
import com.example.authservice.domain.model.Role;
import com.example.authservice.domain.model.User;
import com.example.authservice.domain.repository.UserRepository;
import com.example.authservice.infrastructure.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthApplicationServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final JwtService jwtService = mock(JwtService.class);

    private final AuthApplicationService authService =
            new AuthApplicationService(userRepository, passwordEncoder, jwtService);

    @Test
    void register_shouldCreateUserAndReturnToken() {
        RegisterRequest request = new RegisterRequest(
                "testuser",
                "test@test.com",
                "123456"
        );

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.empty());

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("123456"))
                .thenReturn("hashed-password");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(jwtService.generateToken(any(User.class)))
                .thenReturn("jwt-token");

        var response = authService.register(request);

        assertThat(response.username()).isEqualTo("testuser");
        assertThat(response.email()).isEqualTo("test@test.com");
        assertThat(response.role()).isEqualTo("USER");
        assertThat(response.token()).isEqualTo("jwt-token");

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("123456");
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    void register_shouldThrowWhenUsernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest(
                "testuser",
                "test@test.com",
                "123456"
        );

        User existingUser = user("testuser", "other@test.com", "hash");

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("username already exists");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_shouldThrowWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest(
                "testuser",
                "test@test.com",
                "123456"
        );

        User existingUser = user("another", "test@test.com", "hash");

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.empty());

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("email already exists");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_shouldReturnTokenWhenCredentialsAreValid() {
        LoginRequest request = new LoginRequest(
                "testuser",
                "123456"
        );

        User user = user("testuser", "test@test.com", "hashed-password");

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("123456", "hashed-password"))
                .thenReturn(true);

        when(jwtService.generateToken(user))
                .thenReturn("jwt-token");

        var response = authService.login(request);

        assertThat(response.username()).isEqualTo("testuser");
        assertThat(response.email()).isEqualTo("test@test.com");
        assertThat(response.role()).isEqualTo("USER");
        assertThat(response.token()).isEqualTo("jwt-token");
    }

    @Test
    void login_shouldThrowWhenUserDoesNotExist() {
        LoginRequest request = new LoginRequest(
                "unknown",
                "123456"
        );

        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void login_shouldThrowWhenPasswordDoesNotMatch() {
        LoginRequest request = new LoginRequest(
                "testuser",
                "wrong-password"
        );

        User user = user("testuser", "test@test.com", "hashed-password");

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrong-password", "hashed-password"))
                .thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    private User user(String username, String email, String passwordHash) {
        return new User(
                UUID.randomUUID(),
                username,
                email,
                passwordHash,
                Role.USER,
                LocalDateTime.now()
        );
    }
}