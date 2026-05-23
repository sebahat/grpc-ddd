package com.example.orderservice.domain.idempotency.cache;

import java.time.Duration;
import java.util.Optional;

public interface IdempotencyCacheStore {

    boolean putInProgressIfAbsent(String key, Duration ttl);

    Optional<IdempotencyCacheEntry> get(String key);

    void markCompleted(String key, String orderId, Duration ttl);

    void remove(String key);
}