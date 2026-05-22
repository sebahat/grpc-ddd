package com.example.orderservice;

import com.example.orderservice.application.service.OrderService;
import com.example.orderservice.domain.exception.OrderNotFoundException;
import com.example.orderservice.domain.exception.OutOfStockException;
import com.example.orderservice.domain.idempotency.cache.IdempotencyCacheEntry;
import com.example.orderservice.domain.idempotency.cache.IdempotencyCacheStore;
import com.example.orderservice.domain.idempotency.cache.IdempotencyStatus;
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

import java.time.Duration;
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
    private IdempotencyCacheStore idempotencyCacheStore;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCreateCompletedOrderWhenStockIsReserved() {
        String idempotencyKey = "idem-1";
        String redisKey = "idempotency:orders:" + idempotencyKey;

        OrderItem item = new OrderItem("iphone-15-pro", 2);

        when(idempotencyCacheStore.get(redisKey))
                .thenReturn(Optional.empty());

        when(idempotencyCacheStore.putInProgressIfAbsent(eq(redisKey), any(Duration.class)))
                .thenReturn(true);

        when(inventoryGrpcClient.reserveStock("iphone-15-pro", 2))
                .thenReturn(true);

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.createOrder(idempotencyKey, List.of(item));

        assertNotNull(result.getId());
        assertEquals(1, result.getItems().size());
        assertEquals(OrderItemStatus.COMPLETED, result.getItems().get(0).getStatus());

        verify(inventoryGrpcClient).reserveStock("iphone-15-pro", 2);
        verify(orderRepository).save(any(Order.class));
        verify(idempotencyCacheStore).markCompleted(
                eq(redisKey),
                eq(result.getId()),
                any(Duration.class)
        );
    }

    @Test
    void shouldThrowOutOfStockWhenStockCannotBeReserved() {
        String idempotencyKey = "idem-2";
        String redisKey = "idempotency:orders:" + idempotencyKey;

        OrderItem item = new OrderItem("iphone-15-pro", 2);

        when(idempotencyCacheStore.get(redisKey))
                .thenReturn(Optional.empty());

        when(idempotencyCacheStore.putInProgressIfAbsent(eq(redisKey), any(Duration.class)))
                .thenReturn(true);

        when(inventoryGrpcClient.reserveStock("iphone-15-pro", 2))
                .thenReturn(false);

        assertThrows(
                OutOfStockException.class,
                () -> orderService.createOrder(idempotencyKey, List.of(item))
        );

        verify(inventoryGrpcClient).reserveStock("iphone-15-pro", 2);
        verify(orderRepository, never()).save(any(Order.class));
        verify(idempotencyCacheStore).remove(redisKey);
        verify(idempotencyCacheStore, never())
                .markCompleted(anyString(), anyString(), any(Duration.class));
    }

    @Test
    void shouldReturnExistingOrderWhenIdempotencyKeyAlreadyCompleted() {
        String idempotencyKey = "idem-3";
        String redisKey = "idempotency:orders:" + idempotencyKey;
        String existingOrderId = "order-123";

        OrderItem item = new OrderItem("iphone-15-pro", 1);
        item.confirm();

        Order existingOrder = new Order(List.of(item));
        existingOrder.setId(existingOrderId);
        existingOrder.evaluateStatus();

        when(idempotencyCacheStore.get(redisKey))
                .thenReturn(Optional.of(
                        new IdempotencyCacheEntry(existingOrderId, IdempotencyStatus.COMPLETED)
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

        verify(inventoryGrpcClient, never()).reserveStock(anyString(), anyInt());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenRequestIsInProgress() {
        String idempotencyKey = "idem-4";
        String redisKey = "idempotency:orders:" + idempotencyKey;

        when(idempotencyCacheStore.get(redisKey))
                .thenReturn(Optional.of(
                        new IdempotencyCacheEntry(null, IdempotencyStatus.IN_PROGRESS)
                ));

        assertThrows(
                IllegalStateException.class,
                () -> orderService.createOrder(
                        idempotencyKey,
                        List.of(new OrderItem("iphone-15-pro", 1))
                )
        );

        verify(inventoryGrpcClient, never()).reserveStock(anyString(), anyInt());
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