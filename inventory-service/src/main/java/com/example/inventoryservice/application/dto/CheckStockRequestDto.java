package com.example.inventoryservice.application.dto;

public record CheckStockRequestDto(String productId, int requestedQuantity) { }