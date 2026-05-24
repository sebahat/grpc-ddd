package com.example.authservice.application.service;

import com.example.authservice.application.dto.AuthResponse;
import com.example.authservice.application.dto.LoginRequest;
import com.example.authservice.application.dto.RegisterRequest;
import com.example.authservice.domain.exception.InvalidCredentialsException;
import com.example.authservice.domain.exception.UserAlreadyExistsException;
import com.example.authservice.domain.model.Role;
import com.example.authservice.domain.model.User;
import com.example.authservice.domain.repository.UserRepository;
import com.example.authservice.infrastructure.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthApplicationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthApplicationService(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder,
                                  JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        userRepository.findByUsername(request.username())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("username already exists");
                });

        userRepository.findByEmail(request.email())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("email already exists");
                });

        String passwordHash = passwordEncoder.encode(request.password());

        User user = new User(
                UUID.randomUUID(),
                request.username(),
                request.email(),
                passwordHash,
                Role.USER,
                LocalDateTime.now()
        );

        User saved = userRepository.save(user);

        String token = jwtService.generateToken(saved);

        return new AuthResponse(
                saved.getId().toString(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getRole().name(),
                token
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(InvalidCredentialsException::new);

        boolean matches = passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        );

        if (!matches) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                user.getId().toString(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                token
        );
    }
}