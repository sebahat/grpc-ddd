package com.example.orderservice.domain.order;

import java.util.List;
import java.util.UUID;

public class Order {

    private String id;
    private List<OrderItem> items;
    private OrderStatus status;

    public Order(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        this.id = UUID.randomUUID().toString();
        this.items = items;
        this.status = OrderStatus.PENDING;

        items.forEach(item -> item.setOrderId(this.id));
    }

    public void markCompleted() {
        this.status = OrderStatus.COMPLETED;
    }

    public void markFailed() {
        this.status = OrderStatus.FAILED;
    }

    public void evaluateStatus() {
        boolean anyFailed = items.stream()
                .anyMatch(i -> i.getStatus() == OrderItemStatus.FAILED);

        if (anyFailed) {
            markFailed();
        } else {
            markCompleted();
        }
    }

    public String getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
        this.items.forEach(item -> item.setOrderId(id));
    }
}