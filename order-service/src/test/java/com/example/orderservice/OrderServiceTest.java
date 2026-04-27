package com.example.orderservice;

import com.example.inventoryservice.grpc.CheckStockResponse;
import com.example.inventoryservice.grpc.DecreaseStockResponse;
import com.example.orderservice.application.service.OrderService;
import com.example.orderservice.domain.exception.OrderNotFoundException;
import com.example.orderservice.domain.exception.OutOfStockException;
import com.example.orderservice.domain.idempotency.ProcessedRequest;
import com.example.orderservice.domain.idempotency.ProcessedRequestRepository;
import com.example.orderservice.domain.order.Order;
import com.example.orderservice.domain.order.OrderItem;
import com.example.orderservice.domain.order.OrderItemStatus;
import com.example.orderservice.domain.order.OrderRepository;
import com.example.orderservice.infrastructure.grpc.InventoryGrpcClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

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

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.createOrder(idempotencyKey, List.of(item));

        assertNotNull(result.getId());
        assertEquals(1, result.getItems().size());
        assertEquals(OrderItemStatus.COMPLETED, result.getItems().get(0).getStatus());

        verify(inventoryGrpcClient).checkStock("iphone-15-pro", 2);
        verify(inventoryGrpcClient).decreaseStock("iphone-15-pro", 2);
        verify(orderRepository).save(any(Order.class));
        verify(processedRequestRepository).save(any());
    }

    @Test
    void shouldThrowOutOfStockWhenStockIsNotAvailable() {
        String idempotencyKey = "idem-2";
        OrderItem item = new OrderItem("iphone-15-pro", 2);

        when(processedRequestRepository.findByIdempotencyKey(idempotencyKey))
                .thenReturn(Optional.empty());

        when(inventoryGrpcClient.checkStock("iphone-15-pro", 2))
                .thenReturn(CheckStockResponse.newBuilder()
                        .setInStock(false)
                        .setAvailableQuantity(0)
                        .build());

        assertThrows(OutOfStockException.class,
                () -> orderService.createOrder(idempotencyKey, List.of(item)));

        verify(inventoryGrpcClient).checkStock("iphone-15-pro", 2);
        verify(inventoryGrpcClient, never()).decreaseStock(anyString(), anyInt());
        verify(orderRepository, never()).save(any(Order.class));
        verify(processedRequestRepository, never()).save(any());
    }

    @Test
    void shouldReturnExistingOrderWhenIdempotencyKeyAlreadyProcessed() {
        String idempotencyKey = "idem-3";
        String existingOrderId = "order-123";

        OrderItem item = new OrderItem("iphone-15-pro", 1);
        item.confirm();

        Order existingOrder = new Order(List.of(item));
        existingOrder.setId(existingOrderId);
        existingOrder.evaluateStatus();

        when(processedRequestRepository.findByIdempotencyKey(idempotencyKey))
                .thenReturn(Optional.of(
                        new ProcessedRequest(idempotencyKey, existingOrderId, LocalDateTime.now())
                ));

        when(orderRepository.findById(existingOrderId))
                .thenReturn(Optional.of(existingOrder));

        Order result = orderService.createOrder(
                idempotencyKey,
                List.of(new OrderItem("iphone-15-pro", 1))
        );

        assertEquals(existingOrderId, result.getId());
        assertEquals(1, result.getItems().size());
        assertEquals(OrderItemStatus.COMPLETED, result.getItems().get(0).getStatus());

        verify(inventoryGrpcClient, never()).checkStock(anyString(), anyInt());
        verify(inventoryGrpcClient, never()).decreaseStock(anyString(), anyInt());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void shouldReturnOrderWhenOrderExists() {
        String orderId = "order-1";

        OrderItem item = new OrderItem("iphone-15-pro", 1);
        item.confirm();

        Order order = new Order(List.of(item));
        order.setId(orderId);
        order.evaluateStatus();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        Order result = orderService.getOrder(orderId);

        assertEquals(orderId, result.getId());
        assertEquals(1, result.getItems().size());
        assertEquals(OrderItemStatus.COMPLETED, result.getItems().get(0).getStatus());

        verify(orderRepository).findById(orderId);
    }

    @Test
    void shouldThrowOrderNotFoundWhenOrderDoesNotExist() {
        String orderId = "missing-order";

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,
                () -> orderService.getOrder(orderId));

        verify(orderRepository).findById(orderId);
    }
}