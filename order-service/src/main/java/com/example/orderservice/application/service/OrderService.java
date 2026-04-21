package com.example.orderservice.application.service;

import com.example.inventoryservice.grpc.InventoryServiceGrpc;
import com.example.orderservice.domain.model.OrderItem;
import com.example.orderservice.infastructure.grpc.InventoryGrpcClient;
import jakarta.persistence.criteria.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.orderservice.domain.repository.OrderItemRepository;
import net.devh.boot.grpc.client.inject.GrpcClient;
import java.util.List;
import java.util.stream.Collectors;
import com.example.orderservice.domain.model.OrderItemStatus;
import com.example.inventoryservice.grpc.CheckStockRequest;
import com.example.inventoryservice.grpc.CheckStockResponse;

@Service
public class OrderService {
    private final OrderItemRepository orderItemRepository;
    private final InventoryGrpcClient inventoryClient;

    public OrderService(OrderItemRepository orderItemRepository,
                        InventoryGrpcClient inventoryClient) {
        this.orderItemRepository = orderItemRepository;
        this.inventoryClient = inventoryClient;
    }

    @Transactional
    public List<OrderItem> createOrder(List<OrderItem> items) {

        List<OrderItem> processed = items.stream()
                .map(this::processItem)
                .toList();

        return orderItemRepository.saveAll(processed);
    }

    private OrderItem processItem(OrderItem item) {

        var response = inventoryClient.checkStock(
                item.getProductId(),
                item.getQuantity()
        );

        if (response.getInStock()) {
            item.confirm();
        } else {
            item.fail();
        }

        return item;
    }

    public OrderItem getOrder(String id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

}