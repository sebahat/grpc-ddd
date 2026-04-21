package com.example.orderservice.domain.model;

import java.time.LocalDateTime;

public class ProcessedRequest {

    private String idempotencyKey;
    private String orderId;
    private LocalDateTime createdAt;

    public ProcessedRequest(String idempotencyKey, String orderId, LocalDateTime createdAt) {
        this.idempotencyKey = idempotencyKey;
        this.orderId = orderId;
        this.createdAt = createdAt;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getOrderId() {
        return orderId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}