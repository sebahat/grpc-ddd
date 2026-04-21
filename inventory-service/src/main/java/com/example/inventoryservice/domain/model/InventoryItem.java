package com.example.inventoryservice.domain.model;

public class InventoryItem {

    private Long id;
    private String sku;
    private int quantity;

    public InventoryItem(Long id, String sku, int quantity) {
        this.id = id;
        this.sku = sku;
        this.quantity = quantity;
    }

    public boolean isInStock(int requestedQuantity) {
        return quantity >= requestedQuantity;
    }

    public void decreaseStock(int amount) {
        if (amount < 0) throw new IllegalArgumentException();
        if (quantity < amount) throw new IllegalStateException();
        quantity -= amount;
    }

    public Long getId() { return id; }
    public String getSku() { return sku; }
    public int getQuantity() { return quantity; }
}