package com.example.inventorysyncservice.producer;

import com.example.inventorysyncservice.dto.InventorySyncEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

@Component
public class InventoryEventProducer {

    private static final Logger log =
            LoggerFactory.getLogger(InventoryEventProducer.class);

    private final KafkaTemplate<String, InventorySyncEvent> kafkaTemplate;

    @Value("${kafka.topic.inventory-events}")
    private String topic;

    public InventoryEventProducer(KafkaTemplate<String, InventorySyncEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(InventorySyncEvent event) {
        try {
            kafkaTemplate.send(topic, event.getProductId(), event).get();

            log.info("Producer sent event eventId={}, productId={}, version={}",
                    event.getEventId(),
                    event.getProductId(),
                    event.getVersion());

        } catch (Exception e) {
            log.error("Failed to send Kafka event eventId={}, productId={}",
                    event.getEventId(),
                    event.getProductId(),
                    e);

            throw new RuntimeException("Failed to publish inventory event", e);
        }
    }
}