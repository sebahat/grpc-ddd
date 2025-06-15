package com.example.orderservice.infastructure;

import com.example.orderservice.domain.model.OrderItem;
import com.example.orderservice.domain.repository.OrderItemRepository;
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
    public void save(OrderItem item) {
        jpa.save(item);
    }

    @Override
    public Optional<OrderItem> findById(String id) {
        return jpa.findById(id);
    }
    @Override
    public List<OrderItem> saveAll(List<OrderItem> items) {
        return jpa.saveAll(items);
    }

}
