package com.example.orderservice.application.service;

import com.example.orderservice.domain.exception.OrderNotFoundException;
import com.example.orderservice.domain.exception.OutOfStockException;
import com.example.orderservice.domain.idempotency.cache.IdempotencyCacheStore;
import com.example.orderservice.domain.idempotency.cache.IdempotencyStatus;
import com.example.orderservice.domain.order.Order;
import com.example.orderservice.domain.order.OrderItem;
import com.example.orderservice.domain.order.OrderRepository;
import com.example.orderservice.infrastructure.grpc.InventoryGrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
public class OrderService {

    private static final Duration IN_PROGRESS_TTL = Duration.ofMinutes(10);
    private static final Duration COMPLETED_TTL = Duration.ofHours(24);

    private final OrderRepository orderRepository;
    private final InventoryGrpcClient inventoryClient;
    private final IdempotencyCacheStore idempotencyCacheStore;

    public OrderService(OrderRepository orderRepository,
                        InventoryGrpcClient inventoryClient,
                        IdempotencyCacheStore idempotencyCacheStore) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
        this.idempotencyCacheStore = idempotencyCacheStore;
    }

    @Transactional
    public Order createOrder(String idempotencyKey, List<OrderItem> items) {

        String redisKey = "idempotency:orders:" + idempotencyKey;

        var existing = idempotencyCacheStore.get(redisKey);

        if (existing.isPresent()) {
            if (existing.get().getStatus() == IdempotencyStatus.IN_PROGRESS) {
                throw new IllegalStateException("request already in progress");
            }

            String orderId = existing.get().getOrderId();

            return orderRepository.findById(orderId)
                    .orElseThrow(() -> new OrderNotFoundException(orderId));
        }

        boolean locked = idempotencyCacheStore.putInProgressIfAbsent(
                redisKey,
                IN_PROGRESS_TTL
        );

        if (!locked) {
            throw new IllegalStateException("duplicate request in progress");
        }

        try {
            List<OrderItem> processed = items.stream()
                    .map(this::processItem)
                    .toList();

            Order order = new Order(processed);
            order.evaluateStatus();

            Order saved = orderRepository.save(order);

            idempotencyCacheStore.markCompleted(
                    redisKey,
                    saved.getId(),
                    COMPLETED_TTL
            );

            return saved;

        } catch (Exception ex) {
            idempotencyCacheStore.remove(redisKey);
            throw ex;
        }
    }

    private OrderItem processItem(OrderItem item) {

        boolean reserved = inventoryClient.reserveStock(
                item.getProductId(),
                item.getQuantity()
        );

        if (!reserved) {
            throw new OutOfStockException(item.getProductId());
        }

        item.confirm();
        return item;
    }

    @Transactional(readOnly = true)
    public Order getOrder(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
}