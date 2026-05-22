package com.example.inventoryservice.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "processed_events")
public class ProcessedEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    private String eventId;

    protected ProcessedEventEntity() {
    }

    public ProcessedEventEntity(String eventId) {
        this.eventId = eventId;
    }

    public Long getId() {
        return id;
    }

    public String getEventId() {
        return eventId;
    }
}