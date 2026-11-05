package com.brunomdleal.portfolio.orderservice.repository;

import com.brunomdleal.portfolio.orderservice.AbstractIntegrationTests;
import com.brunomdleal.portfolio.orderservice.enums.OrderStatus;
import com.brunomdleal.portfolio.orderservice.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.brunomdleal.portfolio.orderservice.utils.OrderCreator.generateOrder;
import static com.brunomdleal.portfolio.orderservice.utils.OrderCreator.generateOrders;
import static org.junit.jupiter.api.Assertions.*;

//  Opens a database transaction
@Transactional
public class OrderRepositoryIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Validate the orders saved persists and are returned when searched for them")
    public void save_RetrievesTheSameOrder() {
        String orderId = UUID.randomUUID().toString();
        String orderItem = "Validate the order persists in database";
        int orderQuantity = 2;
        OrderStatus orderStatus = OrderStatus.CONFIRMED;

        assertNotNull(orderRepository.save(generateOrder(orderId, orderQuantity, orderItem)));

        Optional<Order> retrieved = orderRepository.findById(orderId);
        assertTrue(retrieved.isPresent());
        assertEquals(orderId, retrieved.get().id());
        assertTrue(retrieved.get().item().contains(orderItem));
        assertEquals(orderQuantity, retrieved.get().quantity());
        assertEquals(orderStatus, retrieved.get().status());
    }

    @Test
    @DisplayName("Validate the order can be updated by calling the save method with a new order with existent ID")
    public void save_WithExistentID_UpdatesTheOrder() {
        String orderId = UUID.randomUUID().toString();
        String updatedOrderItem = "Validate order is updated";
        int updatedOrderQuantity = 2;
        OrderStatus updatedOrderStatus = OrderStatus.PENDING;

        assertNotNull(orderRepository.save(generateOrder(orderId, 1, "Initial order data")));
        Optional<Order> initialOrder = orderRepository.findById(orderId);
        assertTrue(initialOrder.isPresent());
        assertEquals(orderId, initialOrder.get().id());
        assertNotEquals(updatedOrderItem, initialOrder.get().item());
        assertNotEquals(updatedOrderQuantity, initialOrder.get().quantity());
        assertNotEquals(updatedOrderStatus, initialOrder.get().status());

        assertNotNull(orderRepository.save(new Order(
                orderId,
                updatedOrderItem,
                updatedOrderQuantity,
                updatedOrderStatus
        )));
        Optional<Order> updatedOrder = orderRepository.findById(orderId);
        assertTrue(updatedOrder.isPresent());
        assertEquals(orderId, updatedOrder.get().id());
        assertTrue(updatedOrder.get().item().contains(updatedOrderItem));
        assertEquals(updatedOrderQuantity, updatedOrder.get().quantity());
        assertEquals(updatedOrderStatus, updatedOrder.get().status());
    }

    @Test
    @DisplayName("Validate it returns an empty optional object when searched by an order with a non existent ID")
    public void findBy_WithNonExistentOrderID_ReturnEmptyOptionalObject() {
        Optional<Order> retrieved = orderRepository.findById(UUID.randomUUID().toString());
        assertTrue(retrieved.isEmpty());
    }

    @Test
    @DisplayName("Validate it returns an empty optional object when searched by an order with a malformed ID")
    public void findBy_WithMalformedOrderID_ReturnEmptyOptionalObject() {
        Optional<Order> retrieved = orderRepository.findById("invalid-uuid-format");
        assertTrue(retrieved.isEmpty());
    }

    @Test
    @DisplayName("Validate it returns All saved orders when searched for all")
    public void findAll_ReturnAllTheSavedOrders() {
        int amountOfOrdersToCreate = 3;
        List<Order> ordersToCreate = generateOrders(amountOfOrdersToCreate, "validate it returns all 3 orders");
        List<Order> initialOrdersInDatabase = orderRepository.findAll();
        assertNotNull(initialOrdersInDatabase);
        int initialAmountOfOrdersInDatabase = initialOrdersInDatabase.size();

        ordersToCreate.forEach(order -> assertNotNull(orderRepository.save(order)));

        List<Order> ordersInDatabase = orderRepository.findAll();
        assertNotNull(ordersInDatabase);
        int amountOfOrdersInDatabase = ordersInDatabase.size();
        assertEquals(3, amountOfOrdersInDatabase - initialAmountOfOrdersInDatabase);
        assertTrue(ordersInDatabase.containsAll(ordersToCreate));
    }

}
