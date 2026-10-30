package com.brunomdleal.portfolio.orderservice.repository;

import com.brunomdleal.portfolio.orderservice.enums.OrderStatus;
import com.brunomdleal.portfolio.orderservice.model.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class OrderRepositoryTests {

    private final OrderRepository orderRepository = new OrderRepository();

    @Test
    public void save_WithSuccess() {
        Order order = new Order("Order to check if the save method works", 1);
        assertEquals(order, orderRepository.save(order));
    }

    @Test
    public void findAll_WithSuccess() {
        List<Order> allOrders = saveMultipleOrders(3, "Order to check if the findAll method works.");
        List<Order> result = orderRepository.findAll();
        assertEquals(3, result.size());
        assertTrue(result.containsAll(allOrders));
    }

    @Test
    public void findBy_WithSuccess() {
        saveMultipleOrders(3, "Order to check if the findBy method works.");
        assertEquals(
                generateOrder(1, "Order to check if the findBy method works."),
                orderRepository.findById("1").orElseThrow()
        );
    }

    @Test
    public void findBy_WithNonExistentId_ThrowsNoSuchElementException() {
        saveMultipleOrders(2, "Order to check if the findBy method works.");
        assertThrowsExactly(NoSuchElementException.class, () -> orderRepository.findById("4").orElseThrow());
    }

    private List<Order> saveMultipleOrders(int number, String item) {
        List<Order> orders = new ArrayList<>();

        for (int i = 1; i <= number; i++) {
            orders.add(generateOrder(i, item));
        }
        orders.forEach(orderRepository::save);
        return orders;
    }

    private Order generateOrder(int number, String item) {
        return new Order(String.valueOf(number), item + "#" + number, number, OrderStatus.CONFIRMED);
    }
}
