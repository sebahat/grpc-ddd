package com.example.orderservice.infastructure.persistence.repository;

import com.example.orderservice.domain.model.ProcessedRequest;
import com.example.orderservice.domain.repository.ProcessedRequestRepository;
import com.example.orderservice.infastructure.persistence.entity.ProcessedRequestEntity;
import com.example.orderservice.infastructure.persistence.mapper.ProcessedRequestMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaProcessedRequestRepository implements ProcessedRequestRepository {

    private final SpringDataProcessedRequestRepository jpa;

    public JpaProcessedRequestRepository(SpringDataProcessedRequestRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<ProcessedRequest> findByIdempotencyKey(String idempotencyKey) {
        return jpa.findById(idempotencyKey)
                .map(ProcessedRequestMapper::toDomain);
    }

    @Override
    public ProcessedRequest save(ProcessedRequest request) {
        ProcessedRequestEntity entity = ProcessedRequestMapper.toEntity(request);
        ProcessedRequestEntity saved = jpa.save(entity);
        return ProcessedRequestMapper.toDomain(saved);
    }
}