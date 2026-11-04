package com.brunomdleal.portfolio.orderservice.repository;

import com.brunomdleal.portfolio.orderservice.entity.OrderEntity;
import com.brunomdleal.portfolio.orderservice.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.brunomdleal.portfolio.orderservice.utils.OrderCreator.generateOrder;
import static com.brunomdleal.portfolio.orderservice.utils.OrderCreator.generateOrders;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/*
    Integrates JUnit 5 with Mockito.
    Initializes all fields marked with @Mock
    Performs dependency injection on fields marked with @InjectMocks before tests execute.
* */
@ExtendWith(MockitoExtension.class)
public class OrderRepositoryTests {

    /*
        Creates a mock instance of the SpringDataOrderRepository interface.
        It has no real database backing -> return defined using when(...).
    * */
    @Mock
    private SpringDataOrderRepository jpaRepository;

    /*
        Instantiates and declares an instance of OrderRepository using the mock jpaRepository
        (new OrderRepository(jpaRepository)).
    * */
    @InjectMocks
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Save persists OrderEntity and returns domain Order")
    public void save_WithSuccess() {
        UUID id = UUID.randomUUID();
        Order order = generateOrder(
                id.toString(),
                1,
                "Order to check if the save method works"
        );
        OrderEntity orderInDataBase = OrderEntity.fromDomain(order);
        when(
                jpaRepository.save(
                        any(OrderEntity.class)
                )
        ).thenReturn(
                orderInDataBase
        );

        assertEquals(order, orderRepository.save(order));
        verify(jpaRepository, times(1)).save(any(OrderEntity.class));
        verifyNoMoreInteractions(jpaRepository);
    }

    @Test
    @DisplayName("FindAll retrieves all domains Order")
    public void findAll_WithSuccess() {
        List<Order> allOrders = generateOrders(3, "Order to check if the findAll method works.");
        List<OrderEntity> allOrdersInDataBase = allOrders.stream().map(OrderEntity::fromDomain).toList();
        when(jpaRepository.findAll()).thenReturn(allOrdersInDataBase);
        List<Order> result = orderRepository.findAll();
        assertEquals(3, result.size());
        assertTrue(result.containsAll(allOrders));
        verify(jpaRepository, times(1)).findAll();
        verifyNoMoreInteractions(jpaRepository);
    }

    @Test
    @DisplayName("FindByID retrieves domain Order with that ID")
    public void findByID_WithSuccess() {
        String id = UUID.randomUUID().toString();
        Order order = generateOrder(id, 1, "Order to check if the findBy method works.");
        when(jpaRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(OrderEntity.fromDomain(order)));
        assertEquals(
                order,
                orderRepository.findById(id).orElseThrow()
        );
        verify(jpaRepository, times(1)).findById(any(UUID.class));
        verifyNoMoreInteractions(jpaRepository);
    }

    @Test
    @DisplayName("FindByID returns an empty Optional when called with a non existent ID")
    public void findByID_WithNonExistentId_ReturnsEmptyOptional() {
        when(jpaRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertTrue(orderRepository.findById(UUID.randomUUID().toString()).isEmpty());
        verify(jpaRepository, times(1)).findById(any(UUID.class));
        verifyNoMoreInteractions(jpaRepository);
    }

    @Test
    @DisplayName("FindByID returns an empty Optional when called with an invalid ID")
    public void findByID_WithInvalidId_ReturnsEmptyOptional() {
        assertTrue(orderRepository.findById("InvalidID").isEmpty());
        verifyNoMoreInteractions(jpaRepository);
    }
}
