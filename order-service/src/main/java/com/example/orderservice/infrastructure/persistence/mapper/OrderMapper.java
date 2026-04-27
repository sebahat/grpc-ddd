package com.example.orderservice.infrastructure.persistence.mapper;

import com.example.orderservice.domain.order.Order;
import com.example.orderservice.domain.order.OrderItem;
import com.example.orderservice.infrastructure.persistence.entity.OrderEntity;
import com.example.orderservice.infrastructure.persistence.entity.OrderItemEntity;

import java.time.LocalDateTime;
import java.util.List;

public class OrderMapper {

    public static OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setStatus(order.getStatus());
        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }

    public static List<OrderItemEntity> toItemEntities(Order order) {
        return order.getItems().stream().map(item -> {
            OrderItemEntity e = new OrderItemEntity();
            e.setId(item.getId());
            e.setOrderId(order.getId());
            e.setProductId(item.getProductId());
            e.setQuantity(item.getQuantity());
            e.setStatus(item.getStatus());
            return e;
        }).toList();
    }

    public static Order toDomain(OrderEntity orderEntity, List<OrderItemEntity> items) {

        List<OrderItem> domainItems = items.stream().map(e -> {
            OrderItem item = new OrderItem(
                    e.getProductId(),
                    e.getQuantity()
            );
            item.setId(e.getId());
            item.setOrderId(e.getOrderId());

            if (e.getStatus() != null) {
                switch (e.getStatus()) {
                    case COMPLETED -> item.confirm();
                    case FAILED -> item.fail();
                }
            }

            return item;
        }).toList();

        Order order = new Order(domainItems);
        order.setId(orderEntity.getId());
        order.evaluateStatus();

        return order;
    }
}