package com.example.inventorysyncservice.dto;

public class InventorySyncEvent {

    private String eventId;
    private EventType eventType;
    private String productId;
    private String productName;
    private Integer quantity;
    private Long version;

    public InventorySyncEvent() {
    }

    public InventorySyncEvent(String eventId,
                              EventType eventType,
                              String productId,
                              String productName,
                              Integer quantity,
                              Long version) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.version = version;
    }

    public String getEventId() {
        return eventId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getVersion() {
        return version;
    }
}