package com.example.inventoryservice.application.mapper;

import com.example.inventoryservice.application.dto.CheckStockRequestDto;
import com.example.inventoryservice.grpc.CheckStockResponse;
import com.example.inventoryservice.grpc.CheckStockRequest;
import com.example.inventoryservice.application.dto.CheckStockResponseDto;
public class InventoryMapper {

    /**
     * gRPC’den gelen CheckStockRequest’i DTO’ya dönüştürür.
     *
     * @param grpcRequest Protoc tarafından üretilen CheckStockRequest
     * @return CheckStockRequestDto
     */
    public static CheckStockRequestDto toDto(CheckStockRequest grpcRequest) {
        return new CheckStockRequestDto(
                grpcRequest.getProductId(),
                grpcRequest.getRequestedQuantity()
        );
    }
    /**
     * CheckStockResponseDto’yu gRPC CheckStockResponse’e dönüştürür.
     *
     * @param dto Application katmanından dönen CheckStockResponseDto
     * @return Protoc tarafından üretilen CheckStockResponse.Builder yardımıyla oluşturulmuş nesne
     */
    public static CheckStockResponse toGrpc(CheckStockResponseDto dto) {
        return CheckStockResponse.newBuilder()
                .setInStock(dto.inStock())
                .setAvailableQuantity(dto.availableQuantity())
                .build();
    }

}
