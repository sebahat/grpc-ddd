package com.example.orderservice.domain.idempotency;

import com.example.orderservice.domain.idempotency.ProcessedRequest;

import java.util.Optional;

public interface ProcessedRequestRepository {

    Optional<ProcessedRequest> findByIdempotencyKey(String idempotencyKey);

    ProcessedRequest save(ProcessedRequest request);
}