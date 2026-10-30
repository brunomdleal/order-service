package com.brunomdleal.portfolio.orderservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateOrderRequestTests {

    @Test
    public void createOrderRequestWithSuccess() {
        String item = "Validate if is possible to create an order request";
        CreateOrderRequest orderRequest = new CreateOrderRequest(
                item,
                1
        );
        assertEquals(item, orderRequest.item());
        assertEquals(1, orderRequest.quantity());
    }

    @Test
    public void createOrderRequestWithItemAsNull() {
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new CreateOrderRequest(
                        null,
                        1
                )
        );
    }

    @Test
    public void createOrderRequestWithItemAsEmptyString() {
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new CreateOrderRequest(
                        "",
                        1
                )
        );
    }

    @Test
    public void createOrderRequestWithQuantityLowerThanOne() {
        String item = "Validate if is not possible to create an order request with quantity lower than 1";
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new CreateOrderRequest(
                        item,
                        0
                )
        );
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new CreateOrderRequest(
                        item,
                        -100
                )
        );
    }

}
