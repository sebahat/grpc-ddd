package com.example.inventoryservice.infrastructure.grpc;

import com.example.inventoryservice.application.dto.CheckStockRequestDto;
import com.example.inventoryservice.application.dto.CheckStockResponseDto;
import com.example.inventoryservice.application.service.InventoryApplicationService;
import com.example.inventoryservice.domain.exception.ProductNotFoundException;
import com.example.inventoryservice.grpc.CheckStockRequest;
import com.example.inventoryservice.grpc.CheckStockResponse;
import com.example.inventoryservice.grpc.InventoryServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        try{
        log.info("gRPC checkStock request received sku={}, qty={}",
                request.getProductId(),
                request.getRequestedQuantity());


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

       }catch (ProductNotFoundException ex) {
            log.warn("Product not found in gRPC layer sku={}", request.getProductId());

            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(ex.getMessage())
                            .asRuntimeException());
        } catch (Exception ex){
            log.error("Unexpected error in gRPC checkStock sku={}",
                    request.getProductId(), ex);

            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error")
                            .asRuntimeException()
            );
        }
    }
}