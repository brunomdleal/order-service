package com.brunomdleal.portfolio.orderservice.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

}
