package com.example.inventorysyncservice.service;

import com.example.inventorysyncservice.dto.InventoryEventRequest;
import com.example.inventorysyncservice.producer.InventoryEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InventoryEventService {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventService.class);

    private final InventoryEventProducer inventoryEventProducer;

    public InventoryEventService(InventoryEventProducer inventoryEventProducer) {
        this.inventoryEventProducer = inventoryEventProducer;
    }

    public void publishEvent(InventoryEventRequest request) {

        log.info("Publishing event for productId={}", request.getProductId());

        try {
            inventoryEventProducer.sendEvent(request);
        } catch (Exception e) {
            log.error("Failed to publish event for productId={}", request.getProductId(), e);
            throw e;
        }
    }
}