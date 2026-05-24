package com.example.authservice.infrastructure.security;

import com.example.authservice.domain.model.Role;
import com.example.authservice.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final String SECRET =
            "change-this-secret-key-change-this-secret-key";

    @Test
    void generateToken_shouldContainUserClaims() {
        JwtService jwtService = new JwtService(SECRET, 3600000);

        UUID userId = UUID.randomUUID();

        User user = new User(
                userId,
                "testuser",
                "test@test.com",
                "hashed-password",
                Role.USER,
                LocalDateTime.now()
        );

        String token = jwtService.generateToken(user);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(
                        SECRET.getBytes(StandardCharsets.UTF_8)
                ))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.getSubject()).isEqualTo(userId.toString());
        assertThat(claims.get("username", String.class)).isEqualTo("testuser");
        assertThat(claims.get("role", String.class)).isEqualTo("USER");
        assertThat(claims.getExpiration()).isNotNull();
        assertThat(claims.getIssuedAt()).isNotNull();
    }
}