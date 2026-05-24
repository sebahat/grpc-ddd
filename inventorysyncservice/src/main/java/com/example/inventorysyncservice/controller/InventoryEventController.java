package com.example.inventorysyncservice.controller;

import com.example.inventorysyncservice.dto.InventoryEventRequest;
import com.example.inventorysyncservice.service.InventoryEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory-events")
@Tag(
        name = "Inventory Events",
        description = "Manage stock events published to Kafka"
)
public class InventoryEventController {

    private final InventoryEventService inventoryEventService;

    public InventoryEventController(
            InventoryEventService inventoryEventService
    ) {
        this.inventoryEventService = inventoryEventService;
    }

    @PostMapping("/stock")
    @Operation(
            summary = "Update or insert stock event"
    )
    public String updateStock(
            @Valid @RequestBody InventoryEventRequest request
    ) {
        inventoryEventService.publishEvent(request);
        return "stock event published for product: "
                + request.getProductId();
    }
}