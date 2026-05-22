package com.example.inventorysyncservice.service;

import com.example.inventorysyncservice.dto.EventType;
import com.example.inventorysyncservice.dto.InventoryEventRequest;
import com.example.inventorysyncservice.dto.InventorySyncEvent;
import com.example.inventorysyncservice.producer.InventoryEventProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
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

        ArgumentCaptor<InventorySyncEvent> captor =
                ArgumentCaptor.forClass(InventorySyncEvent.class);

        verify(inventoryEventProducer).sendEvent(captor.capture());

        InventorySyncEvent event = captor.getValue();

        assertNotNull(event.getEventId());
        assertFalse(event.getEventId().isBlank());
        assertEquals(EventType.STOCK_UPDATED, event.getEventType());
        assertEquals("iphone-15-pro", event.getProductId());
        assertEquals("iPhone 15 Pro", event.getProductName());
        assertEquals(25, event.getQuantity());
        assertNotNull(event.getVersion());
        assertTrue(event.getVersion() > 0);
    }
}