package com.example.orderservice;

import com.example.inventoryservice.grpc.CheckStockResponse;
import com.example.inventoryservice.grpc.DecreaseStockResponse;
import com.example.orderservice.application.service.OrderService;
import com.example.orderservice.domain.model.OrderItem;
import com.example.orderservice.domain.model.OrderItemStatus;
import com.example.orderservice.domain.model.ProcessedRequest;
import com.example.orderservice.domain.repository.OrderItemRepository;
import com.example.orderservice.domain.repository.ProcessedRequestRepository;
import com.example.orderservice.infastructure.grpc.InventoryGrpcClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private InventoryGrpcClient inventoryGrpcClient;

    @Mock
    private ProcessedRequestRepository processedRequestRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCreateCompletedOrderWhenStockIsAvailable() {
        String idempotencyKey = "idem-1";
        OrderItem item = new OrderItem("iphone-15-pro", 2);

        when(processedRequestRepository.findByIdempotencyKey(idempotencyKey))
                .thenReturn(Optional.empty());

        when(inventoryGrpcClient.checkStock("iphone-15-pro", 2))
                .thenReturn(CheckStockResponse.newBuilder()
                        .setInStock(true)
                        .setAvailableQuantity(10)
                        .build());

        when(inventoryGrpcClient.decreaseStock("iphone-15-pro", 2))
                .thenReturn(DecreaseStockResponse.newBuilder()
                        .setSuccess(true)
                        .build());

        when(orderItemRepository.saveAll(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<OrderItem> result = orderService.createOrder(idempotencyKey, List.of(item));

        assertEquals(1, result.size());
        assertEquals(OrderItemStatus.COMPLETED, result.get(0).getStatus());

        verify(inventoryGrpcClient).checkStock("iphone-15-pro", 2);
        verify(inventoryGrpcClient).decreaseStock("iphone-15-pro", 2);
        verify(orderItemRepository).saveAll(any());
        verify(processedRequestRepository).save(any());
    }

    @Test
    void shouldFailOrderWhenStockIsNotAvailable() {
        String idempotencyKey = "idem-2";
        OrderItem item = new OrderItem("iphone-15-pro", 2);

        when(processedRequestRepository.findByIdempotencyKey(idempotencyKey))
                .thenReturn(Optional.empty());

        when(inventoryGrpcClient.checkStock("iphone-15-pro", 2))
                .thenReturn(CheckStockResponse.newBuilder()
                        .setInStock(false)
                        .setAvailableQuantity(0)
                        .build());

        when(orderItemRepository.saveAll(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<OrderItem> result = orderService.createOrder(idempotencyKey, List.of(item));

        assertEquals(1, result.size());
        assertEquals(OrderItemStatus.FAILED, result.get(0).getStatus());

        verify(inventoryGrpcClient).checkStock("iphone-15-pro", 2);
        verify(inventoryGrpcClient, never()).decreaseStock(anyString(), anyInt());
        verify(orderItemRepository).saveAll(any());
        verify(processedRequestRepository).save(any());
    }

    @Test
    void shouldReturnExistingOrderWhenIdempotencyKeyAlreadyProcessed() {
        String idempotencyKey = "idem-3";
        String existingOrderId = "order-123";

        OrderItem existingOrder = new OrderItem("iphone-15-pro", 1);
        existingOrder.setId(existingOrderId);
        existingOrder.confirm();

        when(processedRequestRepository.findByIdempotencyKey(idempotencyKey))
                .thenReturn(Optional.of(
                        new ProcessedRequest(idempotencyKey, existingOrderId, LocalDateTime.now())
                ));

        when(orderItemRepository.findById(existingOrderId))
                .thenReturn(Optional.of(existingOrder));

        List<OrderItem> result = orderService.createOrder(
                idempotencyKey,
                List.of(new OrderItem("iphone-15-pro", 1))
        );

        assertEquals(1, result.size());
        assertEquals(existingOrderId, result.get(0).getId());
        assertEquals(OrderItemStatus.COMPLETED, result.get(0).getStatus());

        verify(inventoryGrpcClient, never()).checkStock(anyString(), anyInt());
        verify(inventoryGrpcClient, never()).decreaseStock(anyString(), anyInt());
        verify(orderItemRepository, never()).saveAll(any());
    }
}