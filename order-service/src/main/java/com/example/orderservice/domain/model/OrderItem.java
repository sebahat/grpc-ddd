package com.example.orderservice.domain.model;

import java.util.UUID;

public class OrderItem {

    private String id;
    private String productId;
    private int quantity;
    private OrderItemStatus status;

    public OrderItem(String productId, int quantity) {

        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("productId must not be empty");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }

        this.id = UUID.randomUUID().toString();
        this.productId = productId;
        this.quantity = quantity;
        this.status = OrderItemStatus.PENDING;
    }

    public void confirm() {
        this.status = OrderItemStatus.COMPLETED;
    }

    public void fail() {
        this.status = OrderItemStatus.FAILED;
    }

    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderItemStatus getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }
}