package com.example.orderservice.infrastructure.persistence.repository;



import com.example.orderservice.infrastructure.persistence.entity.ProcessedRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataProcessedRequestRepository
        extends JpaRepository<ProcessedRequestEntity, String> {
}