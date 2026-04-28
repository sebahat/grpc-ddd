package com.example.inventoryservice.infrastructure.persistence.repository;

import com.example.inventoryservice.infrastructure.persistence.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpringDataInventoryItemRepository
        extends JpaRepository<InventoryEntity, Long> {

    Optional<InventoryEntity> findByProductId(String productId);

    @Modifying
    @Query("""
           UPDATE InventoryEntity i
           SET i.quantity = i.quantity - :quantity
           WHERE i.productId = :productId
           AND i.quantity >= :quantity
           """)
    int reserveStock(@Param("productId") String productId,
                     @Param("quantity") int quantity);
}