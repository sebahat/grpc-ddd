package com.example.orderservice.domain.idempotency.cache;

public class IdempotencyCacheEntry {

    private String orderId;

    private IdempotencyStatus status;

    public IdempotencyCacheEntry() {

    }

    public IdempotencyCacheEntry(String orderId, IdempotencyStatus status) {

        this.orderId = orderId;

        this.status = status;

    }

    public String getOrderId() {

        return orderId;

    }

    public IdempotencyStatus getStatus() {

        return status;

    }

}