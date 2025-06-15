package com.example.orderservice.infastructure;

import com.example.orderservice.domain.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataOrderRepository extends JpaRepository<OrderItem, String> {
}
