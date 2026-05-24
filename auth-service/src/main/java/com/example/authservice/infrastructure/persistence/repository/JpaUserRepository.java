package com.example.authservice.infrastructure.persistence.repository;

import com.example.authservice.domain.model.User;
import com.example.authservice.domain.repository.UserRepository;
import com.example.authservice.infrastructure.persistence.entity.UserEntity;
import com.example.authservice.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaUserRepository implements UserRepository {

    private final SpringDataUserRepository springRepo;

    public JpaUserRepository(SpringDataUserRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springRepo.findByEmail(email)
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return springRepo.findByUsername(username)
                .map(UserMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity saved = springRepo.save(entity);
        return UserMapper.toDomain(saved);
    }
}