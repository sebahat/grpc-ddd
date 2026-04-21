package com.example.inventoryservice.domain.exception;

public class InsufficientStockException extends  RuntimeException{
    public InsufficientStockException(String sku, int requested, int available) {
        super("Insufficient stock for sku=" + sku +
                ", requested=" + requested +
                ", available=" + available);
    }
}
