package com.example.inventoryservice.infrastructure.persistence.repository;


import com.example.inventoryservice.infrastructure.persistence.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
/**
 * Spring Data JPA tarafından otomatik implement edilecek arayüz.
 */


/**
 * Sadece DB (Entity) bilir.
 * Domain ile hiçbir ilişkisi yok.
 */
public interface SpringDataInventoryItemRepository
        extends JpaRepository<InventoryEntity, Long> {

    Optional<InventoryEntity> findByProductId(String productId);
}