package com.example.orderservice.interfaces.dto;

import com.example.orderservice.domain.order.Order;
import com.example.orderservice.domain.order.OrderItem;

import java.util.List;

public class OrderResponseDto {

    private String orderId;
    private String status;
    private List<Item> items;

    public static OrderResponseDto fromDomain(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.orderId = order.getId();
        dto.status = order.getStatus().name();
        dto.items = order.getItems().stream().map(Item::from).toList();
        return dto;
    }

    public String getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public List<Item> getItems() { return items; }

    public static class Item {
        private String id;
        private String productId;
        private int quantity;
        private String status;

        public static Item from(OrderItem item) {
            Item dto = new Item();
            dto.id = item.getId();
            dto.productId = item.getProductId();
            dto.quantity = item.getQuantity();
            dto.status = item.getStatus().name();
            return dto;
        }

        public String getId() { return id; }
        public String getProductId() { return productId; }
        public int getQuantity() { return quantity; }
        public String getStatus() { return status; }
    }
}