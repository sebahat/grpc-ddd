package com.example.inventorysyncservice.service;

import com.example.inventorysyncservice.dto.InventoryEventRequest;
import com.example.inventorysyncservice.dto.InventorySyncEvent;
import com.example.inventorysyncservice.producer.InventoryEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InventoryEventService {

    private static final Logger log =
            LoggerFactory.getLogger(InventoryEventService.class);

    private final InventoryEventProducer inventoryEventProducer;

    public InventoryEventService(InventoryEventProducer inventoryEventProducer) {
        this.inventoryEventProducer = inventoryEventProducer;
    }

    public void publishEvent(InventoryEventRequest request) {

        InventorySyncEvent event = new InventorySyncEvent(
                UUID.randomUUID().toString(),
                request.getEventType(),
                request.getProductId(),
                request.getProductName(),
                request.getQuantity(),
                System.currentTimeMillis()
        );

        log.info("Publishing event eventId={}, productId={}, version={}",
                event.getEventId(),
                event.getProductId(),
                event.getVersion());

        try {
            inventoryEventProducer.sendEvent(event);
        } catch (Exception e) {
            log.error("Failed to publish event for productId={}",
                    request.getProductId(),
                    e);
            throw e;
        }
    }
}