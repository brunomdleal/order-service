package com.brunomdleal.portfolio.orderservice.dto;

public record CreateOrderRequest(String item, int quantity) {

    public CreateOrderRequest {
        if (item == null || item.isBlank()) {
            throw new IllegalArgumentException("Item name cannot be empty");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
    }

}
