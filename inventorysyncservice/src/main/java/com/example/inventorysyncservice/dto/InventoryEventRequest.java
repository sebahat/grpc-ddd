package com.example.inventorysyncservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InventoryEventRequest {

    @NotNull(message = "eventType is required")
    private EventType eventType;

    @NotBlank(message = "productId is required")
    private String productId;

    @NotBlank(message = "productName is required")
    private String productName;

    @NotNull(message = "quantity is required")
    @Min(value = 0, message = "quantity must be greater than or equal to 0")
    private Integer quantity;

    public InventoryEventRequest() {
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}