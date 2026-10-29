package com.brunomdleal.portfolio.orderservice.model;

import com.brunomdleal.portfolio.orderservice.enums.OrderStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TestOrder {

    @Test
    public void createAFullOrderWithSuccess() {
        String orderId = UUID.randomUUID().toString();
        String orderItem = "Validate if is possible to create an order with all the arguments";
        Order order = new Order(
                orderId,
                orderItem,
                1,
                OrderStatus.CONFIRMED
        );
        assertEquals(orderId, order.id());
        assertEquals(orderItem, order.item());
        assertEquals(1, order.quantity());
        assertEquals(OrderStatus.CONFIRMED, order.status());
    }

    @Test
    public void createAnOrderOnlyWithItemAndQuantityWithSuccess() {
        String orderItem = "Validate if is possible to create an order with just the item and quantity arguments";
        Order order = new Order(
                orderItem,
                1
        );
        assertNotNull(order.id());
        assertFalse(order.id().isBlank());
        assertEquals(orderItem, order.item());
        assertEquals(1, order.quantity());
        assertEquals(OrderStatus.PENDING, order.status());
    }

    @Test
    public void notPossibleToCreateAnOrderWithQuantityLowerThanOne() {
        String orderItem = "Validate if is not possible to create an order with quantity lower than 1";
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new Order(
                        UUID.randomUUID().toString(),
                        orderItem,
                        0,
                        OrderStatus.CONFIRMED
                )
        );
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new Order(
                        UUID.randomUUID().toString(),
                        orderItem,
                        -100,
                        OrderStatus.CONFIRMED
                )
        );
    }
}
