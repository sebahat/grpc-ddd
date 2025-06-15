package com.example.orderservice.domain.repository;

import com.example.orderservice.domain.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository {
    void save(OrderItem item);
    Optional<OrderItem> findById(String id);
    List<OrderItem> saveAll(List<OrderItem> items);
}