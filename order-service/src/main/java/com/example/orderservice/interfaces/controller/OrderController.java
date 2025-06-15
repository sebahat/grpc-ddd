package com.example.orderservice.interfaces.controller;

import com.example.orderservice.application.service.OrderService;
import com.example.orderservice.domain.model.OrderItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/orders")

public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Tag(name = "Orders", description = "Order API işlemleri")
    @PostMapping
    public ResponseEntity<List<OrderItem>> createOrder(@RequestBody List<OrderItem> items) {
        logger.info("POST /orders çağrıldı, gelen items sayısı: {}", items.size());
        return ResponseEntity.ok(orderService.createOrder(items));
    }

    @Operation(summary = "Sipariş oluştur", description = "Verilen ürünlerden sipariş oluşturur")
    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }


}