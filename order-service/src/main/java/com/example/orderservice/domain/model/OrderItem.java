package com.example.orderservice.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    private String id;

    @Schema(description = "Ürün ID'si", example = "12345")
    @Column(nullable = false)
    private String productId;
    @Schema(description = "Ürün miktarı", example = "2")
    @Column(nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderItemStatus status;

    public OrderItem() {}

    public OrderItem(String id, String productId, int quantity, OrderItemStatus status) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.status = status;
    }

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID().toString();
        if (this.status == null) {
            this.status = OrderItemStatus.PENDING;
        }
    }

    // Getter and Setter methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }
}
