package com.example.inventoryservice.infrastructure.grpc;

import com.example.inventoryservice.grpc.CheckStockRequest;
import com.example.inventoryservice.grpc.CheckStockResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class InventoryServiceImpl extends com.example.inventoryservice.grpc.InventoryServiceGrpc.InventoryServiceImplBase {

    @Override
    public void checkStock(CheckStockRequest request, StreamObserver<CheckStockResponse> responseObserver) {
        CheckStockResponse response = CheckStockResponse.newBuilder()
                .setInStock(true)
                .setAvailableQuantity(20)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
