package com.example.orderservice.infastructure.persistence.repository;



import com.example.orderservice.infastructure.persistence.entity.ProcessedRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataProcessedRequestRepository
        extends JpaRepository<ProcessedRequestEntity, String> {
}