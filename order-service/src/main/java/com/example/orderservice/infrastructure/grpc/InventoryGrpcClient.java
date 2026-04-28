package com.example.orderservice.infrastructure.grpc;

import com.example.inventoryservice.grpc.InventoryServiceGrpc;
import com.example.inventoryservice.grpc.ReserveStockRequest;
import com.example.inventoryservice.grpc.ReserveStockResponse;
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
    @Retry(name = "inventoryRetry", fallbackMethod = "fallbackReserveStock")
    @CircuitBreaker(name = "inventoryCircuitBreaker", fallbackMethod = "fallbackReserveStock")
    public boolean reserveStock(String productId, int qty) {

        ReserveStockRequest request = ReserveStockRequest.newBuilder()
                .setProductId(productId)
                .setQuantity(qty)
                .build();

        try {
            ReserveStockResponse response = stub
                    .withDeadlineAfter(2, TimeUnit.SECONDS)
                    .reserveStock(request);

            return response.getSuccess();

        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new ProductNotFoundException(productId);
            }

            throw ex;
        }
    }


    private boolean fallbackReserveStock(String productId, int qty, Throwable ex) {
        if (ex instanceof ProductNotFoundException) {
            throw (ProductNotFoundException) ex;
        }

        return false;
    }

}