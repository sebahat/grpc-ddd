package com.example.orderservice.domain.order;

import com.example.orderservice.domain.order.Order;

import java.util.Optional;

public interface OrderRepository {

    Optional<Order> findById(String id);

    Order save(Order order);

}