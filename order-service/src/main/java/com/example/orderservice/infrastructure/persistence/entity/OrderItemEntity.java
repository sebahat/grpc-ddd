package com.example.orderservice.infrastructure.persistence.entity;

import com.example.orderservice.domain.order.OrderItemStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItemEntity {

    @Id
    private String id;

    @Column(name = "order_id")
    private String orderId;

    private String productId;
    private int quantity;

    @Enumerated(EnumType.STRING)
    private OrderItemStatus status;

    public OrderItemEntity() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public OrderItemStatus getStatus() { return status; }
    public void setStatus(OrderItemStatus status) { this.status = status; }
}