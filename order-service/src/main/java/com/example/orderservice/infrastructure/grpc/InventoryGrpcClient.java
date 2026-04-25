package com.example.orderservice.infrastructure.grpc;

import com.example.inventoryservice.grpc.CheckStockRequest;
import com.example.inventoryservice.grpc.CheckStockResponse;
import com.example.inventoryservice.grpc.DecreaseStockRequest;
import com.example.inventoryservice.grpc.DecreaseStockResponse;
import com.example.inventoryservice.grpc.InventoryServiceGrpc;
import com.example.orderservice.domain.exception.ProductNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class InventoryGrpcClient {

    @GrpcClient("inventory-service")
    private InventoryServiceGrpc.InventoryServiceBlockingStub stub;

    @Retry(name = "inventoryRetry", fallbackMethod = "fallbackCheckStock")
    @CircuitBreaker(name = "inventoryCircuitBreaker", fallbackMethod = "fallbackCheckStock")
    public CheckStockResponse checkStock(String productId, int qty) {

        CheckStockRequest request = CheckStockRequest.newBuilder()
                .setProductId(productId)
                .setRequestedQuantity(qty)
                .build();

        try {
            return stub
                    .withDeadlineAfter(2, TimeUnit.SECONDS)
                    .checkStock(request);
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new ProductNotFoundException(productId);
            }

            throw ex;
        }
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
        if (ex instanceof ProductNotFoundException) {
            throw (ProductNotFoundException) ex;
        }

        return CheckStockResponse.newBuilder()
                .setInStock(false)
                .setAvailableQuantity(0)
                .build();
    }
}