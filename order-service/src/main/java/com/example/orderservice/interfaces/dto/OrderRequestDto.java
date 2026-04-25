package com.example.orderservice.interfaces.dto;


import com.example.orderservice.domain.model.OrderItem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class OrderRequestDto {

    @NotBlank(message = "productId is required")
    private String productId;

    @Min(value = 1, message = "quantity must be greater than 0")
    private int quantity;

    public OrderRequestDto() {}

    public OrderRequestDto(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

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