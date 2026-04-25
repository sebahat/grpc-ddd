package com.example.inventoryservice.infrastructure.kafka.consumer;

import com.example.inventoryservice.application.service.InventoryApplicationService;
import com.example.inventoryservice.infrastructure.kafka.dto.EventType;
import com.example.inventoryservice.infrastructure.kafka.dto.InventorySyncEvent;
import com.example.inventoryservice.infrastructure.kafka.validation.InventorySyncEventValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(InventoryEventConsumer.class);

    private final InventoryApplicationService applicationService;
    private final InventorySyncEventValidator validator;

    public InventoryEventConsumer(InventoryApplicationService applicationService,
                                  InventorySyncEventValidator validator) {
        this.applicationService = applicationService;
        this.validator = validator;
    }

    @KafkaListener(
            topics = "${app.kafka.topic.inventory-events}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(InventorySyncEvent event) {

        try {
            validator.validate(event);

            if (event.getEventType() != EventType.STOCK_UPDATED) {
                log.warn("Unsupported eventType={}, ignoring event", event.getEventType());
                return;
            }

            log.info("Kafka event received eventType={}, productId={}, quantity={}",
                    event.getEventType(),
                    event.getProductId(),
                    event.getQuantity());

            applicationService.upsertStock(
                    event.getProductId(),
                    event.getProductName(),
                    event.getQuantity()
            );

            log.info("Kafka event processed successfully for productId={}",
                    event.getProductId());

        } catch (IllegalArgumentException e) {
            log.warn("Invalid Kafka event for productId={}, reason={}",
                    event != null ? event.getProductId() : null,
                    e.getMessage());
        } catch (Exception e) {
            log.error("Failed to process Kafka event for productId={}",
                    event != null ? event.getProductId() : null,
                    e);
        }
    }
}