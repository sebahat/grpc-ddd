package com.example.inventoryservice.application.service;

import com.example.inventoryservice.application.dto.CheckStockRequestDto;
import com.example.inventoryservice.application.dto.CheckStockResponseDto;
import com.example.inventoryservice.domain.exception.ProductNotFoundException;
import com.example.inventoryservice.domain.model.InventoryItem;
import com.example.inventoryservice.domain.repository.InventoryItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryApplicationService {

    private static final Logger log =
            LoggerFactory.getLogger(InventoryApplicationService.class);

    private final InventoryItemRepository repository;

    public InventoryApplicationService(InventoryItemRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public CheckStockResponseDto checkStock(CheckStockRequestDto request) {

        log.info("CheckStock request received productId={}, qty={}",
                request.productId(),
                request.requestedQuantity());

        InventoryItem item = repository.findByProductId(request.productId())
                .orElseThrow(() -> {
                    log.warn("Product not found productId={}", request.productId());
                    return new ProductNotFoundException(request.productId());
                });

        boolean inStock = item.isInStock(request.requestedQuantity());

        log.info("CheckStock result productId={}, available={}, inStock={}",
                item.getProductId(),
                item.getQuantity(),
                inStock);

        return new CheckStockResponseDto(
                inStock,
                item.getQuantity()
        );
    }

    @Transactional
    public void decreaseStock(String productId, int quantity) {

        log.info("DecreaseStock request received productId={}, quantity={}",
                productId, quantity);

        InventoryItem item = repository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        item.decreaseStock(quantity);

        repository.save(item);

        log.info("Stock decreased successfully for productId={}, remainingQuantity={}",
                item.getProductId(),
                item.getQuantity());
    }

    @Transactional
    public void upsertStock(String productId, String productName, int quantity) {

        log.info("Upsert stock request received productId={}, productName={}, quantity={}",
                productId, productName, quantity);

        InventoryItem item = repository.findByProductId(productId)
                .orElse(null);

        if (item == null) {
            log.info("Creating new inventory item for productId={}", productId);

            InventoryItem newItem = new InventoryItem(
                    null,
                    productId,
                    productName,
                    quantity
            );

            repository.save(newItem);
            return;
        }

        log.info("Updating existing inventory item for productId={}", productId);

        item.updateFromSync(productName, quantity);

        repository.save(item);
    }
}

