package com.example.inventoryservice.infrastructure.persistence.repository;

import com.example.inventoryservice.infrastructure.persistence.entity.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataProcessedEventRepository
        extends JpaRepository<ProcessedEventEntity, Long> {

    boolean existsByEventId(String eventId);
}