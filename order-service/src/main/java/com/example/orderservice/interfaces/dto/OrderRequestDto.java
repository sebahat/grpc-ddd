package com.example.orderservice.interfaces.dto;


import com.example.orderservice.domain.model.OrderItem;

public class OrderRequestDto {

    private String productId;
    private int quantity;

    public OrderRequestDto() {}

    public OrderRequestDto(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // DTO → DOMAIN
    public OrderItem toDomain() {
        return new OrderItem(productId, quantity);
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}