package com.example.orderservice.interfaces.dto;

import com.example.orderservice.domain.model.OrderItem;
public class OrderResponseDto {

    private String id;
    private String productId;
    private int quantity;
    private String status;

    public static OrderResponseDto fromDomain(OrderItem item) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.id = item.getId();
        dto.productId = item.getProductId();
        dto.quantity = item.getQuantity();
        dto.status = item.getStatus().name();
        return dto;
    }

    public String getId() { return id; }
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public String getStatus() { return status; }
}