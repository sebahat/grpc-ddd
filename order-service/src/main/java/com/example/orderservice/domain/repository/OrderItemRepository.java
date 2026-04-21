package com.example.orderservice.domain.repository;

import com.example.orderservice.domain.model.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository {

    Optional<OrderItem> findById(String id);

    List<OrderItem> saveAll(List<OrderItem> items);
}