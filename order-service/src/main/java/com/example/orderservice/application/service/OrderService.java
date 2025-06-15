package com.example.orderservice.application.service;

import com.example.inventoryservice.grpc.InventoryServiceGrpc;
import com.example.orderservice.domain.model.OrderItem;
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

   @GrpcClient("inventory-service")
    private InventoryServiceGrpc.InventoryServiceBlockingStub inventoryStub;


    public OrderService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public List<OrderItem> createOrder(List<OrderItem> orderItems) {
        // Envanter kontrolü yap
        List<OrderItem> checkedItems = orderItems.stream()
                .map(this::checkAndMarkItem)
                .collect(Collectors.toList());

        // Order nesnesi oluştur
       return orderItemRepository.saveAll(checkedItems);

    }

    public OrderItem getOrder(String id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    private OrderItem checkAndMarkItem(OrderItem item) {
        CheckStockRequest request = CheckStockRequest.newBuilder()
                .setProductId(item.getProductId())
                .setRequestedQuantity(item.getQuantity())
                .build();

       CheckStockResponse response = inventoryStub.checkStock(request);

        // Stok var mı yok mu kontrol et
        if (response.getInStock()) {
            item.setStatus(OrderItemStatus.COMPLETED);
        } else {
            item.setStatus(OrderItemStatus.FAILED);
        }

        return item;
    }


}