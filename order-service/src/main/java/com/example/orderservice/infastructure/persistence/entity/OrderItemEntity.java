package com.example.orderservice.infastructure.persistence.entity;

import com.example.orderservice.domain.model.OrderItemStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItemEntity {

    @Id
    private String id;

    private String productId;
    private int quantity;

    @Enumerated(EnumType.STRING)
    private OrderItemStatus status;

    public OrderItemEntity() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public OrderItemStatus getStatus() { return status; }
    public void setStatus(OrderItemStatus status) { this.status = status; }
}