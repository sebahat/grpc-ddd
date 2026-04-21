package com.example.inventoryservice.infrastructure.grpc;


import com.example.inventoryservice.domain.exception.ProductNotFoundException;
import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcAdvice
public class GlobalGrpcExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalGrpcExceptionHandler.class);

    @GrpcExceptionHandler(ProductNotFoundException.class)
    public Status handleProductNotFound(ProductNotFoundException ex) {
        log.warn("Product not found: {}", ex.getMessage());
        return Status.NOT_FOUND.withDescription(ex.getMessage());
    }

    @GrpcExceptionHandler(IllegalArgumentException.class)
    public Status handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        return Status.INVALID_ARGUMENT.withDescription(ex.getMessage());
    }

    @GrpcExceptionHandler(Exception.class)
    public Status handleGeneric(Exception ex) {
        log.error("Unexpected error", ex);
        return Status.INTERNAL.withDescription("Internal server error");
    }
}