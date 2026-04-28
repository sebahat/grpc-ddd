package com.example.inventoryservice.infrastructure.persistence.repository;


import com.example.inventoryservice.infrastructure.persistence.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface SpringDataInventoryItemRepository
        extends JpaRepository<InventoryEntity, Long> {

    Optional<InventoryEntity> findByProductId(String productId);
}