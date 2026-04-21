package com.example.inventoryservice.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_items")
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private String productId;
    private int quantity;

    public InventoryEntity() {
    }

    public InventoryEntity(Long id, String productId , int quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getProductId() { return productId; }

    public int getQuantity() {
        return quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductId(String productId) { this.productId = productId; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}