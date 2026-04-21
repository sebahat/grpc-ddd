package com.example.inventoryservice.application.validation;
public final class CheckStockValidator {

    private CheckStockValidator() {
    }

    public static void validate(String productId, int requestedQuantity) {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("productId must not be empty");
        }

        if (requestedQuantity <= 0) {
            throw new IllegalArgumentException("requestedQuantity must be greater than 0");
        }
    }
}