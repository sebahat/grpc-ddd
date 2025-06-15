package com.example.inventoryservice.application.service;

import com.example.inventoryservice.domain.model.InventoryItem;
import com.example.inventoryservice.domain.repository.InventoryItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckStockUseCase {

    private final InventoryItemRepository inventoryItemRepository;

    // Constructor (Lombok'suz)
    public CheckStockUseCase(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @Transactional(readOnly = true)
    public boolean isInStock(String productId, int requestedQuantity) {
        return inventoryItemRepository.findBySku(productId)
                .map(item -> item.getQuantity() >= requestedQuantity)
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public int getAvailableQuantity(String productId) {
        return inventoryItemRepository.findBySku(productId)
                .map(InventoryItem::getQuantity)
                .orElse(0);
    }
}
