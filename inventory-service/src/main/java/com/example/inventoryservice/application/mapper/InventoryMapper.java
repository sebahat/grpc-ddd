package com.example.inventoryservice.application.mapper;

import com.example.inventoryservice.domain.model.InventoryItem;
import com.example.inventoryservice.infrastructure.persistence.entity.InventoryEntity;

public class InventoryMapper {

    public static InventoryItem toDomain(InventoryEntity entity) {
        if (entity == null) return null;

        return new InventoryItem(
                entity.getId(),
                entity.getProductId(),
                entity.getProductName(),
                entity.getQuantity()
        );
    }

    public static InventoryEntity toEntity(InventoryItem domain) {
        if (domain == null) return null;

        return new InventoryEntity(
                domain.getId(),
                domain.getProductId(),
                domain.getProductName(),
                domain.getQuantity()
        );
    }
}