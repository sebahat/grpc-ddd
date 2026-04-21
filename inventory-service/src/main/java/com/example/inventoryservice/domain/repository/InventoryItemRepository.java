package com.example.inventoryservice.domain.repository;

import com.example.inventoryservice.domain.model.InventoryItem;

import java.util.Optional;

public interface InventoryItemRepository {

    Optional<InventoryItem> findBySku(String sku);

    InventoryItem save(InventoryItem item);
}