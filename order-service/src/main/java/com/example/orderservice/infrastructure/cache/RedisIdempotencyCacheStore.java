package com.example.orderservice.infrastructure.cache;

import com.example.orderservice.domain.idempotency.cache.IdempotencyCacheEntry;
import com.example.orderservice.domain.idempotency.cache.IdempotencyCacheStore;
import com.example.orderservice.domain.idempotency.cache.IdempotencyStatus;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class RedisIdempotencyCacheStore implements IdempotencyCacheStore {

    private final StringRedisTemplate redisTemplate;

    public RedisIdempotencyCacheStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean putInProgressIfAbsent(String key, Duration ttl) {
        Boolean result = redisTemplate.opsForValue()
                .setIfAbsent(key, "IN_PROGRESS", ttl);

        return Boolean.TRUE.equals(result);
    }

    @Override
    public Optional<IdempotencyCacheEntry> get(String key) {
        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            return Optional.empty();
        }

        if ("IN_PROGRESS".equals(value)) {
            return Optional.of(
                    new IdempotencyCacheEntry(null, IdempotencyStatus.IN_PROGRESS)
            );
        }

        return Optional.of(
                new IdempotencyCacheEntry(value, IdempotencyStatus.COMPLETED)
        );
    }

    @Override
    public void markCompleted(String key, String orderId, Duration ttl) {
        redisTemplate.opsForValue().set(key, orderId, ttl);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }
}