package com.example.inventoryservice.domain.model;

public class InventoryItem {

    private Long id;
    private String productId;
    private String productName;
    private int quantity;
    private Long lastEventVersion;

    public InventoryItem(
            Long id,
            String productId,
            String productName,
            int quantity,
            Long lastEventVersion
    ) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.lastEventVersion = lastEventVersion;
    }

    public boolean isInStock(int requestedQuantity) {
        return quantity >= requestedQuantity;
    }

    public void decreaseStock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than 0");
        }

        if (quantity < amount) {
            throw new IllegalStateException("not enough stock");
        }

        quantity -= amount;
    }

    public void updateFromSync(
            String productName,
            int quantity,
            Long version
    ) {
        this.productName = productName;
        this.quantity = quantity;
        this.lastEventVersion = version;
    }

    public Long getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getLastEventVersion() {
        return lastEventVersion;
    }
}