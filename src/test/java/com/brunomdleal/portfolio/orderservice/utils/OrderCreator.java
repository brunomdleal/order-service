package com.brunomdleal.portfolio.orderservice.utils;

import com.brunomdleal.portfolio.orderservice.enums.OrderStatus;
import com.brunomdleal.portfolio.orderservice.model.Order;

import java.util.List;
import java.util.UUID;

import static java.util.stream.IntStream.rangeClosed;

public final class OrderCreator {

    public static List<Order> generateOrders(int number, String item) {
        return rangeClosed(1, number)
                .mapToObj(i -> generateOrder(UUID.randomUUID().toString(), i, item))
                .toList();
    }

    public static Order generateOrder(String id, int number, String item) {
        return new Order(id, item + " #" + number, number, OrderStatus.CONFIRMED);
    }

    public static String generateOrderName(int number, String item) {
        return  item + " #" + number;
    }

}
