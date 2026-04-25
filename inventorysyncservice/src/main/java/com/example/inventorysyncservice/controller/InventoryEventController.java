package com.example.inventorysyncservice.controller;
import com.example.inventorysyncservice.dto.InventoryEventRequest;
import com.example.inventorysyncservice.service.InventoryEventService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventory-events")
public class InventoryEventController {

    private final InventoryEventService inventoryEventService;

    public InventoryEventController(InventoryEventService inventoryEventService) {
        this.inventoryEventService = inventoryEventService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "inventory-sync-service is running";
    }

    @PostMapping
    public String publishInventoryEvent(@Valid @RequestBody InventoryEventRequest request) {
        inventoryEventService.publishEvent(request);
        return "event received for product: " + request.getProductId();
    }
}