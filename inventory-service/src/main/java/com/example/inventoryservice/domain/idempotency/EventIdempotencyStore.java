package com.example.inventoryservice.domain.idempotency;

import java.time.Duration;

public interface EventIdempotencyStore {

    boolean putIfAbsent(String key, Duration ttl);

    void remove(String key);
}