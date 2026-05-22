package com.example.inventoryservice.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_items")
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", unique = true, nullable = false)
    private String productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "last_event_version")
    private Long lastEventVersion;

    public InventoryEntity() {
    }

    public InventoryEntity(
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setLastEventVersion(Long lastEventVersion) {
        this.lastEventVersion = lastEventVersion;
    }
}