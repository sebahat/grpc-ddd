package com.example.inventorysyncservice.service;

import com.example.inventorysyncservice.dto.EventType;
import com.example.inventorysyncservice.dto.InventoryEventRequest;
import com.example.inventorysyncservice.producer.InventoryEventProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InventoryEventServiceTest {

    @Mock
    private InventoryEventProducer inventoryEventProducer;

    @InjectMocks
    private InventoryEventService inventoryEventService;

    @Test
    void shouldPublishInventoryEvent() {
        InventoryEventRequest request = new InventoryEventRequest();
        request.setEventType(EventType.STOCK_UPDATED);
        request.setProductId("iphone-15-pro");
        request.setProductName("iPhone 15 Pro");
        request.setQuantity(25);

        inventoryEventService.publishEvent(request);

        verify(inventoryEventProducer).sendEvent(request);
    }
}