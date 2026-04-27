package com.example.orderservice.infrastructure.persistence.repository;

import com.example.orderservice.domain.order.Order;
import com.example.orderservice.domain.order.OrderRepository;
import com.example.orderservice.infrastructure.persistence.entity.OrderEntity;
import com.example.orderservice.infrastructure.persistence.entity.OrderItemEntity;
import com.example.orderservice.infrastructure.persistence.mapper.OrderMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaOrderRepository implements OrderRepository {

    private final SpringDataOrderRepository orderJpa;
    private final SpringDataOrderItemRepository itemJpa;

    public JpaOrderRepository(SpringDataOrderRepository orderJpa,
                              SpringDataOrderItemRepository itemJpa) {
        this.orderJpa = orderJpa;
        this.itemJpa = itemJpa;
    }

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = OrderMapper.toEntity(order);
        OrderEntity savedOrderEntity = orderJpa.save(orderEntity);

        List<OrderItemEntity> itemEntities = OrderMapper.toItemEntities(order);
        List<OrderItemEntity> savedItemEntities = itemJpa.saveAll(itemEntities);

        return OrderMapper.toDomain(savedOrderEntity, savedItemEntities);
    }

    @Override
    public Optional<Order> findById(String id) {
        return orderJpa.findById(id)
                .map(orderEntity -> {
                    List<OrderItemEntity> itemEntities = itemJpa.findByOrderId(id);
                    return OrderMapper.toDomain(orderEntity, itemEntities);
                });
    }
}