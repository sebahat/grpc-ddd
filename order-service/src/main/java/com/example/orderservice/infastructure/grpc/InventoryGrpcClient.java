package com.example.orderservice.infastructure.grpc;

import com.example.inventoryservice.grpc.CheckStockRequest;
import com.example.inventoryservice.grpc.CheckStockResponse;
import com.example.inventoryservice.grpc.DecreaseStockRequest;
import com.example.inventoryservice.grpc.DecreaseStockResponse;
import com.example.inventoryservice.grpc.InventoryServiceGrpc;
import io.github.resilience4j.retry.annotation.Retry;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class InventoryGrpcClient {

    @GrpcClient("inventory-service")
    private InventoryServiceGrpc.InventoryServiceBlockingStub stub;

    @Retry(name = "inventoryRetry", fallbackMethod = "fallbackCheckStock")
    public CheckStockResponse checkStock(String productId, int qty) {

        CheckStockRequest request = CheckStockRequest.newBuilder()
                .setProductId(productId)
                .setRequestedQuantity(qty)
                .build();

        return stub
                .withDeadlineAfter(2, TimeUnit.SECONDS)
                .checkStock(request);
    }

    public DecreaseStockResponse decreaseStock(String productId, int qty) {

        DecreaseStockRequest request = DecreaseStockRequest.newBuilder()
                .setProductId(productId)
                .setQuantity(qty)
                .build();

        return stub
                .withDeadlineAfter(2, TimeUnit.SECONDS)
                .decreaseStock(request);
    }

    private CheckStockResponse fallbackCheckStock(String productId, int qty, Throwable ex) {
        return CheckStockResponse.newBuilder()
                .setInStock(false)
                .setAvailableQuantity(0)
                .build();
    }
}