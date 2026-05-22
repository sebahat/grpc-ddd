package com.example.inventoryservice.infrastructure.kafka.consumer;

import com.example.inventoryservice.application.service.InventoryApplicationService;
import com.example.inventoryservice.infrastructure.kafka.dto.EventType;
import com.example.inventoryservice.infrastructure.kafka.dto.InventorySyncEvent;
import com.example.inventoryservice.infrastructure.kafka.validation.InventorySyncEventValidator;
import com.example.inventoryservice.infrastructure.persistence.entity.ProcessedEventEntity;
import com.example.inventoryservice.infrastructure.persistence.repository.SpringDataProcessedEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InventoryEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(InventoryEventConsumer.class);

    private final InventoryApplicationService applicationService;
    private final InventorySyncEventValidator validator;
    private final SpringDataProcessedEventRepository processedEventRepository;

    public InventoryEventConsumer(InventoryApplicationService applicationService,
                                  InventorySyncEventValidator validator,
                                  SpringDataProcessedEventRepository processedEventRepository) {
        this.applicationService = applicationService;
        this.validator = validator;
        this.processedEventRepository = processedEventRepository;
    }

    @Transactional
    @KafkaListener(
            topics = "${app.kafka.topic.inventory-events}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(InventorySyncEvent event) {

        validator.validate(event);

        if (processedEventRepository.existsByEventId(event.getEventId())) {
            log.info("Duplicate Kafka event ignored eventId={}, productId={}",
                    event.getEventId(),
                    event.getProductId());
            return;
        }

        if (event.getEventType() != EventType.STOCK_UPDATED) {
            log.warn("Unsupported eventType={}, ignoring event", event.getEventType());
            return;
        }

        log.info("Kafka event received eventId={}, eventType={}, productId={}, quantity={}, version={}",
                event.getEventId(),
                event.getEventType(),
                event.getProductId(),
                event.getQuantity(),
                event.getVersion());

        applicationService.upsertStock(
                event.getProductId(),
                event.getProductName(),
                event.getQuantity(),
                event.getVersion()
        );

        processedEventRepository.save(
                new ProcessedEventEntity(event.getEventId())
        );

        log.info("Kafka event processed successfully eventId={}, productId={}, version={}",
                event.getEventId(),
                event.getProductId(),
                event.getVersion());
    }
}