package com.example.inventoryservice.infrastructure.grpc;

import com.example.inventoryservice.application.dto.CheckStockRequestDto;
import com.example.inventoryservice.application.dto.CheckStockResponseDto;
import com.example.inventoryservice.application.service.InventoryApplicationService;
import com.example.inventoryservice.grpc.CheckStockRequest;
import com.example.inventoryservice.grpc.CheckStockResponse;
import com.example.inventoryservice.grpc.InventoryServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.inventoryservice.grpc.DecreaseStockRequest;
import com.example.inventoryservice.grpc.DecreaseStockResponse;

import com.example.inventoryservice.application.validation.CheckStockValidator;

@GrpcService
public class InventoryServiceImpl extends InventoryServiceGrpc.InventoryServiceImplBase {

    private final InventoryApplicationService applicationService;

    private static final Logger log =
            LoggerFactory.getLogger(InventoryServiceImpl.class);

    public InventoryServiceImpl(InventoryApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public void checkStock(CheckStockRequest request,
                           StreamObserver<CheckStockResponse> responseObserver) {

        log.info("gRPC checkStock request received productId={}, qty={}",
                request.getProductId(),
                request.getRequestedQuantity());

        CheckStockValidator.validate(
                request.getProductId(),
                request.getRequestedQuantity()
        );

        CheckStockRequestDto dto = new CheckStockRequestDto(
                request.getProductId(),
                request.getRequestedQuantity()
        );

        CheckStockResponseDto result = applicationService.checkStock(dto);

        CheckStockResponse response = CheckStockResponse.newBuilder()
                .setInStock(result.inStock())
                .setAvailableQuantity(result.availableQuantity())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void decreaseStock(DecreaseStockRequest request,
                              StreamObserver<DecreaseStockResponse> responseObserver) {

        applicationService.decreaseStock(
                request.getProductId(),
                request.getQuantity()
        );

        DecreaseStockResponse response = DecreaseStockResponse.newBuilder()
                .setSuccess(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}