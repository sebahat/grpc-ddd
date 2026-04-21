package com.example.inventoryservice.domain.model;

public class InventoryItem {

    private Long id;
    private String productId;
    private int quantity;

    public InventoryItem(Long id, String productId, int quantity) {
        this.id = id;
        this.productId = productId;
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
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
}