package com.brunomdleal.portfolio.orderservice.model;

import com.brunomdleal.portfolio.orderservice.enums.OrderStatus;

import java.util.UUID;

public record Order(String id, String item, int quantity, OrderStatus status) {

    public Order {
        if (id == null || id.isBlank()) {
            id = System.currentTimeMillis() + "-" + UUID.randomUUID();
        }
        if (status == null) {
            status = OrderStatus.PENDING;
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
    }

    public Order(String item, int quantity) {
        this(null, item, quantity, null);
    }

}
