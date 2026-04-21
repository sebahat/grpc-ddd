package com.example.orderservice.infastructure.persistence.mapper;
import com.example.orderservice.domain.model.ProcessedRequest;
import com.example.orderservice.infastructure.persistence.entity.ProcessedRequestEntity;

public class ProcessedRequestMapper {

    public static ProcessedRequest toDomain(ProcessedRequestEntity entity) {
        if (entity == null) return null;

        return new ProcessedRequest(
                entity.getIdempotencyKey(),
                entity.getOrderId(),
                entity.getCreatedAt()
        );
    }

    public static ProcessedRequestEntity toEntity(ProcessedRequest domain) {
        if (domain == null) return null;

        return new ProcessedRequestEntity(
                domain.getIdempotencyKey(),
                domain.getOrderId(),
                domain.getCreatedAt()
        );
    }
}