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



import com.example.orderservice.application.service.OrderService;
import com.example.orderservice.interfaces.dto.OrderRequestDto;
import com.example.orderservice.interfaces.dto.OrderResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger log =
            LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<List<OrderResponseDto>> createOrder(
            @RequestBody List<OrderRequestDto> request) {

        log.info("Create order request received. itemCount={}", request.size());

        var result = orderService.createOrder(
                request.stream()
                        .map(dto -> dto.toDomain())
                        .toList()
        );

        var response = result.stream()
                .map(OrderResponseDto::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable String id) {

        log.info("Get order request. id={}", id);

        var order = orderService.getOrder(id);

        return ResponseEntity.ok(
                OrderResponseDto.fromDomain(order)
        );
    }
}