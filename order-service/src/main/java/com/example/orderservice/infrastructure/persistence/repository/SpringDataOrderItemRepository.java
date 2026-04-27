package com.example.orderservice.infrastructure.persistence.repository;

import com.example.orderservice.infrastructure.persistence.entity.OrderItemEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataOrderItemRepository extends JpaRepository<OrderItemEntity, String> {

    List<OrderItemEntity> findByOrderId(String orderId);

}