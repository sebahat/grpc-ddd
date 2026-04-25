
package com.example.inventoryservice.infrastructure.persistence.repository;
import com.example.inventoryservice.application.mapper.InventoryMapper;
import com.example.inventoryservice.domain.model.InventoryItem;
import com.example.inventoryservice.domain.repository.InventoryItemRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;



import com.example.inventoryservice.infrastructure.persistence.entity.InventoryEntity;


/**
 * Domain repository implementasyonu.
 * Mapping burada yapılır.
 */
@Repository
public class JpaInventoryItemRepository implements InventoryItemRepository {

    private final SpringDataInventoryItemRepository springRepo;

    public JpaInventoryItemRepository(SpringDataInventoryItemRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Optional<InventoryItem> findByProductId(String productId) {
        return springRepo.findByProductId(productId)
                .map(InventoryMapper::toDomain);
    }

    @Override
    public InventoryItem save(InventoryItem item) {
        InventoryEntity entity = InventoryMapper.toEntity(item);
        InventoryEntity saved = springRepo.save(entity);
        return InventoryMapper.toDomain(saved);
    }
}