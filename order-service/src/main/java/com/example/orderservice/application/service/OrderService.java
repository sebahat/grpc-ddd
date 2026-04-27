package com.example.orderservice.application.service;

import com.example.orderservice.domain.exception.OrderNotFoundException;
import com.example.orderservice.domain.exception.OutOfStockException;
import com.example.orderservice.domain.idempotency.ProcessedRequest;
import com.example.orderservice.domain.idempotency.ProcessedRequestRepository;
import com.example.orderservice.domain.order.Order;
import com.example.orderservice.domain.order.OrderItem;
import com.example.orderservice.domain.order.OrderRepository;
import com.example.orderservice.infrastructure.grpc.InventoryGrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryGrpcClient inventoryClient;
    private final ProcessedRequestRepository processedRequestRepository;

    public OrderService(OrderRepository orderRepository,
                        InventoryGrpcClient inventoryClient,
                        ProcessedRequestRepository processedRequestRepository) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
        this.processedRequestRepository = processedRequestRepository;
    }

    @Transactional
    public Order createOrder(String idempotencyKey, List<OrderItem> items) {

        var existing = processedRequestRepository.findByIdempotencyKey(idempotencyKey);

        if (existing.isPresent()) {
            String orderId = existing.get().getOrderId();

            return orderRepository.findById(orderId)
                    .orElseThrow(() -> new OrderNotFoundException(orderId));
        }

        List<OrderItem> processed = items.stream()
                .map(this::processItem)
                .toList();

        Order order = new Order(processed);
        order.evaluateStatus();

        Order saved = orderRepository.save(order);

        try {
            processedRequestRepository.save(
                    new ProcessedRequest(
                            idempotencyKey,
                            saved.getId(),
                            LocalDateTime.now()
                    )
            );
        } catch (Exception ex) {
            var alreadyProcessed = processedRequestRepository.findByIdempotencyKey(idempotencyKey);

            if (alreadyProcessed.isPresent()) {
                String existingOrderId = alreadyProcessed.get().getOrderId();

                return orderRepository.findById(existingOrderId)
                        .orElseThrow(() -> new OrderNotFoundException(existingOrderId));
            }

            throw ex;
        }

        return saved;
    }

    private OrderItem processItem(OrderItem item) {

        var response = inventoryClient.checkStock(
                item.getProductId(),
                item.getQuantity()
        );

        if (!response.getInStock()) {
            throw new OutOfStockException(item.getProductId());
        }

        inventoryClient.decreaseStock(
                item.getProductId(),
                item.getQuantity()
        );

        item.confirm();
        return item;
    }

    @Transactional(readOnly = true)
    public Order getOrder(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
}