package com.example.inventoryservice.infrastructure.persistence;

import com.example.inventoryservice.domain.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Spring Data JPA tarafından otomatik implement edilecek arayüz.
 */
@Repository
public interface SpringDataInventoryItemRepository extends JpaRepository<InventoryItem,Long> {

    Optional<InventoryItem> findBySku(String sku);
}
