package com.example.inventoryservice.infrastructure.cache;

import com.example.inventoryservice.domain.idempotency.EventIdempotencyStore;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisEventIdempotencyStore implements EventIdempotencyStore {

    private final StringRedisTemplate redisTemplate;

    public RedisEventIdempotencyStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean putIfAbsent(String key, Duration ttl) {
        Boolean result = redisTemplate.opsForValue()
                .setIfAbsent(key, "PROCESSED", ttl);

        return Boolean.TRUE.equals(result);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }
}