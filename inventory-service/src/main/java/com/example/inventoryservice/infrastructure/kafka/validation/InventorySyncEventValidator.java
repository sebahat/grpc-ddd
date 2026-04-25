package com.example.inventoryservice.infrastructure.kafka.validation;

import com.example.inventoryservice.infrastructure.kafka.dto.EventType;
import com.example.inventoryservice.infrastructure.kafka.dto.InventorySyncEvent;
import org.springframework.stereotype.Component;

@Component
public class InventorySyncEventValidator {

    public void validate(InventorySyncEvent event) {

        if (event == null) {
            throw new IllegalArgumentException("event must not be null");
        }

        if (event.getEventType() == null) {
            throw new IllegalArgumentException("eventType must not be null");
        }

        switch (event.getEventType()) {
            case STOCK_UPDATED:
                break;
            default:
                throw new IllegalArgumentException("Unsupported eventType: " + event.getEventType());
        }

        if (event.getProductId() == null || event.getProductId().isBlank()) {
            throw new IllegalArgumentException("productId must not be empty");
        }

        if (event.getProductName() == null || event.getProductName().isBlank()) {
            throw new IllegalArgumentException("productName must not be empty");
        }

        if (event.getQuantity() == null || event.getQuantity() < 0) {
            throw new IllegalArgumentException("quantity must be greater than or equal to 0");
        }
    }
}