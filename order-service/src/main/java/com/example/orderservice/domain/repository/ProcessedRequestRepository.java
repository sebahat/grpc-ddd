package com.example.orderservice.domain.repository;

import com.example.orderservice.domain.model.ProcessedRequest;

import java.util.Optional;

public interface ProcessedRequestRepository {

    Optional<ProcessedRequest> findByIdempotencyKey(String idempotencyKey);

    ProcessedRequest save(ProcessedRequest request);
}