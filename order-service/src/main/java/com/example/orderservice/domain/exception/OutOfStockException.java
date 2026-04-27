package com.example.orderservice.domain.exception;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException(String productId) {
        super("Out of stock for product: " + productId);
    }
}