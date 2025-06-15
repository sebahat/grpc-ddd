package com.example.inventoryservice.infrastructure.persistence;

import com.example.inventoryservice.domain.model.InventoryItem;
import com.example.inventoryservice.domain.repository.InventoryItemRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaInventoryItemRepository implements InventoryItemRepository {

    private final SpringDataInventoryItemRepository springRepo;

    public JpaInventoryItemRepository(SpringDataInventoryItemRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Optional<InventoryItem> findBySku(String sku) {
        return springRepo.findBySku(sku);
    }

    @Override
    public InventoryItem save(InventoryItem item) {
        return springRepo.save(item);
    }
}
