package com.example.inventoryservice.infrastructure.kafka.consumer;

import com.example.inventoryservice.application.service.InventoryApplicationService;
import com.example.inventoryservice.domain.idempotency.EventIdempotencyStore;
import com.example.inventoryservice.infrastructure.kafka.dto.EventType;
import com.example.inventoryservice.infrastructure.kafka.dto.InventorySyncEvent;
import com.example.inventoryservice.infrastructure.kafka.validation.InventorySyncEventValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Component
public class InventoryEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(InventoryEventConsumer.class);

    private static final Duration EVENT_TTL =
            Duration.ofDays(7);

    private final InventoryApplicationService applicationService;
    private final InventorySyncEventValidator validator;
    private final EventIdempotencyStore eventIdempotencyStore;

    public InventoryEventConsumer(
            InventoryApplicationService applicationService,
            InventorySyncEventValidator validator,
            EventIdempotencyStore eventIdempotencyStore
    ) {
        this.applicationService = applicationService;
        this.validator = validator;
        this.eventIdempotencyStore = eventIdempotencyStore;
    }

    @Transactional
    @KafkaListener(
            topics = "${app.kafka.topic.inventory-events}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(InventorySyncEvent event) {

        validator.validate(event);

        String redisKey =
                "idempotency:inventory-events:" + event.getEventId();

        boolean locked = eventIdempotencyStore.putIfAbsent(
                redisKey,
                EVENT_TTL
        );

        if (!locked) {
            log.info(
                    "Duplicate Kafka event ignored eventId={}, productId={}",
                    event.getEventId(),
                    event.getProductId()
            );
            return;
        }

        try {
            if (event.getEventType() != EventType.STOCK_UPDATED) {
                log.warn(
                        "Unsupported eventType={}, ignoring event",
                        event.getEventType()
                );
                return;
            }

            log.info(
                    "Kafka event received eventId={}, productId={}, version={}",
                    event.getEventId(),
                    event.getProductId(),
                    event.getVersion()
            );

            applicationService.upsertStock(
                    event.getProductId(),
                    event.getProductName(),
                    event.getQuantity(),
                    event.getVersion()
            );

            log.info(
                    "Kafka event processed successfully eventId={}, productId={}",
                    event.getEventId(),
                    event.getProductId()
            );

        } catch (Exception ex) {
            eventIdempotencyStore.remove(redisKey);
            throw ex;
        }
    }
}