package com.example.orderservice.infastructure.persistence.repository;

import com.example.orderservice.domain.model.OrderItem;
import com.example.orderservice.domain.repository.OrderItemRepository;
import com.example.orderservice.infastructure.persistence.entity.OrderItemEntity;
import com.example.orderservice.infastructure.persistence.mapper.OrderItemMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaOrderRepository implements OrderItemRepository {

    private final SpringDataOrderRepository jpa;

    public JpaOrderRepository(SpringDataOrderRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<OrderItem> saveAll(List<OrderItem> items) {
        List<OrderItemEntity> entities = items.stream()
                .map(OrderItemMapper::toEntity)
                .toList();

        List<OrderItemEntity> saved = jpa.saveAll(entities);

        return saved.stream()
                .map(OrderItemMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<OrderItem> findById(String id) {
        return jpa.findById(id)
                .map(OrderItemMapper::toDomain);
    }


}