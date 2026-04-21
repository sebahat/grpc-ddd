package com.example.orderservice.application.service;

import com.example.orderservice.domain.model.OrderItem;
import com.example.orderservice.domain.model.ProcessedRequest;
import com.example.orderservice.domain.repository.OrderItemRepository;
import com.example.orderservice.domain.repository.ProcessedRequestRepository;
import com.example.orderservice.infastructure.grpc.InventoryGrpcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderItemRepository orderItemRepository;
    private final InventoryGrpcClient inventoryClient;
    private final ProcessedRequestRepository processedRequestRepository;

    public OrderService(OrderItemRepository orderItemRepository,
                        InventoryGrpcClient inventoryClient,
                        ProcessedRequestRepository processedRequestRepository) {
        this.orderItemRepository = orderItemRepository;
        this.inventoryClient = inventoryClient;
        this.processedRequestRepository = processedRequestRepository;
    }

    public List<OrderItem> createOrder(String idempotencyKey, List<OrderItem> items) {

        var existing = processedRequestRepository.findByIdempotencyKey(idempotencyKey);

        if (existing.isPresent()) {
            String orderId = existing.get().getOrderId();

            return List.of(
                    orderItemRepository.findById(orderId)
                            .orElseThrow(() -> new RuntimeException("Order not found"))
            );
        }

        List<OrderItem> processed = items.stream()
                .map(this::processItem)
                .toList();

        List<OrderItem> saved = orderItemRepository.saveAll(processed);

        String orderId = saved.get(0).getId();

        try {
            processedRequestRepository.save(
                    new ProcessedRequest(
                            idempotencyKey,
                            orderId,
                            LocalDateTime.now()
                    )
            );
        } catch (Exception ex) {
            var alreadyProcessed = processedRequestRepository.findByIdempotencyKey(idempotencyKey);

            if (alreadyProcessed.isPresent()) {
                String existingOrderId = alreadyProcessed.get().getOrderId();

                return List.of(
                        orderItemRepository.findById(existingOrderId)
                                .orElseThrow(() -> new RuntimeException("Order not found"))
                );
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
            item.fail();
            return item;
        }

        inventoryClient.decreaseStock(
                item.getProductId(),
                item.getQuantity()
        );

        item.confirm();
        return item;
    }

    public OrderItem getOrder(String id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}