package com.example.orderservice.infastructure.grpc;



import com.example.inventoryservice.grpc.CheckStockRequest;
import com.example.inventoryservice.grpc.CheckStockResponse;
import com.example.inventoryservice.grpc.InventoryServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;
import io.github.resilience4j.retry.annotation.Retry;
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
                .withDeadlineAfter(2, java.util.concurrent.TimeUnit.SECONDS)
                .checkStock(request);
    }

    private CheckStockResponse fallbackCheckStock(String productId, int qty, Throwable ex) {

        return CheckStockResponse.newBuilder()
                .setInStock(false)
                .setAvailableQuantity(0)
                .build();
    }
}