package com.example.inventoryservice.application.mapper;

import com.example.inventoryservice.application.dto.CheckStockRequestDto;
import com.example.inventoryservice.domain.model.InventoryItem;
import com.example.inventoryservice.grpc.CheckStockResponse;
import com.example.inventoryservice.grpc.CheckStockRequest;
import com.example.inventoryservice.application.dto.CheckStockResponseDto;
import com.example.inventoryservice.infrastructure.persistence.entity.InventoryEntity;

public class InventoryMapper {


    public static InventoryItem toDomain(InventoryEntity entity) {
        if (entity == null) return null;

        return new InventoryItem(
                entity.getId(),
                entity.getSku(),
                entity.getQuantity()
        );
    }


    public static InventoryEntity toEntity(InventoryItem domain) {
        if (domain == null) return null;

        return new InventoryEntity(
                domain.getId(),
                domain.getSku(),
                domain.getQuantity()
        );
    }
}