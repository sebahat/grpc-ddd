package com.example.orderservice.infastructure.persistence.repository;


import com.example.orderservice.infastructure.persistence.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataOrderRepository extends JpaRepository<OrderItemEntity, String> {
}
