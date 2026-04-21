package com.example.orderservice.infastructure.persistence.mapper;

import com.example.orderservice.domain.model.OrderItem;
import com.example.orderservice.domain.model.OrderItemStatus;
import com.example.orderservice.infastructure.persistence.entity.OrderItemEntity;

public class OrderItemMapper {

    public static OrderItem toDomain(OrderItemEntity e) {
        OrderItem item = new OrderItem(e.getProductId(), e.getQuantity());
        item.setId(e.getId());
        if (e.getStatus() != null) {
            if (e.getStatus() == OrderItemStatus.COMPLETED) item.confirm();
            if (e.getStatus() == OrderItemStatus.FAILED) item.fail();
        }
        return item;
    }

    public static OrderItemEntity toEntity(OrderItem d) {
        OrderItemEntity e = new OrderItemEntity();
        e.setId(d.getId());
        e.setProductId(d.getProductId());
        e.setQuantity(d.getQuantity());
        e.setStatus(d.getStatus());
        return e;
    }
}