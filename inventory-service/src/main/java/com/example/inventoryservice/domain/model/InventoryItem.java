package com.example.inventoryservice.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
@Table(name = "inventory_items")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    // Parametresiz constructor
    public InventoryItem() {
    }

    // Parametreli constructor (all args)
    public InventoryItem(Long id, String sku, int quantity) {
        this.id = id;
        this.sku = sku;
        this.quantity = quantity;
    }

    // Builder pattern varsa ayrı yazılır, burada klasik setter/getter var

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Stok bilgisini kontrol eden bir business metodu örneği:
    public boolean isInStock(int requestedQuantity) {
        return this.quantity >= requestedQuantity;
    }

    // Stok güncelleme metodu:
    public void decreaseStock(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.quantity < amount) {
            throw new IllegalStateException("Not enough stock");
        }
        this.quantity -= amount;
    }
}
