package com.example.inventoryservice;

import com.example.inventoryservice.application.dto.CheckStockRequestDto;
import com.example.inventoryservice.application.dto.CheckStockResponseDto;
import com.example.inventoryservice.application.service.InventoryApplicationService;
import com.example.inventoryservice.domain.exception.ProductNotFoundException;
import com.example.inventoryservice.domain.model.InventoryItem;
import com.example.inventoryservice.domain.repository.InventoryItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryApplicationServiceTest {

    @Mock
    private InventoryItemRepository repository;

    @InjectMocks
    private InventoryApplicationService inventoryApplicationService;

    @Test
    void shouldReturnInStockWhenEnoughQuantityExists() {
        CheckStockRequestDto request = new CheckStockRequestDto("iphone-15-pro", 2);
        InventoryItem item = new InventoryItem(1L, "iphone-15-pro", 10);

        when(repository.findByProductId("iphone-15-pro"))
                .thenReturn(Optional.of(item));

        CheckStockResponseDto response = inventoryApplicationService.checkStock(request);

        assertTrue(response.inStock());
        assertEquals(10, response.availableQuantity());
    }

    @Test
    void shouldThrowProductNotFoundWhenItemDoesNotExist() {
        CheckStockRequestDto request = new CheckStockRequestDto("missing-product", 1);

        when(repository.findByProductId("missing-product"))
                .thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> inventoryApplicationService.checkStock(request));
    }

    @Test
    void shouldDecreaseStockAndSaveItem() {
        InventoryItem item = new InventoryItem(1L, "iphone-15-pro", 10);

        when(repository.findByProductId("iphone-15-pro"))
                .thenReturn(Optional.of(item));
        when(repository.save(any(InventoryItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        inventoryApplicationService.decreaseStock("iphone-15-pro", 3);

        ArgumentCaptor<InventoryItem> captor = ArgumentCaptor.forClass(InventoryItem.class);
        verify(repository).save(captor.capture());

        InventoryItem savedItem = captor.getValue();
        assertEquals(7, savedItem.getQuantity());
        assertEquals("iphone-15-pro", savedItem.getProductId());
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenQuantityIsNotEnough() {
        InventoryItem item = new InventoryItem(1L, "iphone-15-pro", 2);

        when(repository.findByProductId("iphone-15-pro"))
                .thenReturn(Optional.of(item));

        assertThrows(IllegalStateException.class,
                () -> inventoryApplicationService.decreaseStock("iphone-15-pro", 5));

        verify(repository, never()).save(any());
    }
}