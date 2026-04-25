package com.example.orderservice.infrastructure.persistence.repository;


import com.example.orderservice.infrastructure.persistence.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataOrderRepository extends JpaRepository<OrderItemEntity, String> {
}
