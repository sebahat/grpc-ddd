package com.example.inventorysyncservice.producer;
import com.example.inventorysyncservice.dto.InventoryEventRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


@Component
public class InventoryEventProducer {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventProducer.class);

    private final KafkaTemplate<String, InventoryEventRequest> kafkaTemplate;

    @Value("${kafka.topic.inventory-events}")
    private String topic;

    public InventoryEventProducer(KafkaTemplate<String, InventoryEventRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(InventoryEventRequest request) {
        try {
            kafkaTemplate.send(topic, request).get();
            log.info("Producer sent event for productId={}", request.getProductId());
        } catch (Exception e) {
            log.error("Failed to send Kafka event for productId={}", request.getProductId(), e);
            throw new RuntimeException("Failed to publish inventory event", e);
        }
    }
}