package com.example.inventoryservice.application.dto;

public record CheckStockResponseDto(
        boolean inStock,
        int availableQuantity
) { }
