package com.example.orderservice.interfaces.controller;

import com.example.orderservice.application.service.OrderService;
import com.example.orderservice.interfaces.dto.OrderRequestDto;
import com.example.orderservice.interfaces.dto.OrderResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {

    private static final Logger log =
            LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            summary = "Create order",
            description = "Creates an order after checking stock availability through Inventory Service via gRPC."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order created successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "Stock not available")
    })
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @Parameter(description = "Unique key to prevent duplicate order creation", required = true)
            @RequestHeader("Idempotency-Key")
            @NotBlank(message = "Idempotency-Key header is required")
            String idempotencyKey,

            @RequestBody
            @NotEmpty(message = "order items must not be empty")
            List<@Valid OrderRequestDto> request) {

        log.info("Create order request received. itemCount={}, idempotencyKey={}",
                request.size(), idempotencyKey);

        var order = orderService.createOrder(
                idempotencyKey,
                request.stream()
                        .map(OrderRequestDto::toDomain)
                        .toList()
        );

        return ResponseEntity.ok(OrderResponseDto.fromDomain(order));
    }

    @Operation(
            summary = "Get order by id",
            description = "Returns an order by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order found",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(
            @Parameter(description = "Order id", required = true)
            @PathVariable String id) {

        log.info("Get order request. id={}", id);

        var order = orderService.getOrder(id);

        return ResponseEntity.ok(OrderResponseDto.fromDomain(order));
    }
}