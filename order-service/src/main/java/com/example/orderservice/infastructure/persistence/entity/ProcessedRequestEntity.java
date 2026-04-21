package com.example.orderservice.infastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "processed_requests")
public class ProcessedRequestEntity {

    @Id
    @Column(name = "idempotency_key")
    private String idempotencyKey;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public ProcessedRequestEntity() {
    }

    public ProcessedRequestEntity(String idempotencyKey, String orderId, LocalDateTime createdAt) {
        this.idempotencyKey = idempotencyKey;
        this.orderId = orderId;
        this.createdAt = createdAt;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}