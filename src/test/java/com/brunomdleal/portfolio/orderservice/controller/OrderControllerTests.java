package com.brunomdleal.portfolio.orderservice.controller;

import com.brunomdleal.portfolio.orderservice.dto.CreateOrderRequest;
import com.brunomdleal.portfolio.orderservice.enums.OrderStatus;
import com.brunomdleal.portfolio.orderservice.model.Order;
import com.brunomdleal.portfolio.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
    @WebMvcTest:
    - Spring Boot automatically builds a slice of the application context
    - Spring Boot automatically pre-configured MockMvc bean
    - Spring Boot Web automatically configure an ObjectMapper by default with standard settings
 */
@WebMvcTest(OrderController.class)
public class OrderControllerTests {

    /*
        MockMvc: HTTP requester
        @Autowire:
        - Injects pre-configured MockMvc bean into the test
     */
    @Autowired
    private MockMvc mockMvc;
    /*
        ObjectMapper: core library class from Jackson used to:
        - convert Java objects into JSON strings (serialization)
        - JSON strings back into Java objects (deserialization)
        @Autowire:
        - Injects ObjectMapper configured by Spring Boot Web into the test
     */
    @Autowired
    private ObjectMapper objectMapper;

    //    Create a mock of OrderRepository
    @MockitoBean
    private OrderRepository orderRepository;

    /**
     * Given I Have 0 orders in the "DB"
     * When I request to get all the orders I should receive empty list
     * Then I validate the GET method return 200 OK
     * And I validate the GET method return an empty list
     * Then I validate the GET method call the function "orderRepository.findAll()" once
     * And I validate the GET method does not call any more orderRepository functions
     */
    @Test
    @DisplayName("GET /api/v1/orders should return 200 OK with empty list")
    void getAllOrders_WithoutAnyOrderStored_ShouldReturnEmptyListOfOrders() throws Exception {
//        Given I have 0 new orders in the "DB"
        when(orderRepository.findAll()).thenReturn(List.of());
//        When I perform the GET /api/v1/orders request
        mockMvc.perform(get("/api/v1/orders")
                        .accept(MediaType.APPLICATION_JSON))
//        Then I get the two existent orders
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
//        And I validate the method ".findAll()" was only called once
        verify(orderRepository).findAll();
//        And I validate there were no other method calls
        verifyNoMoreInteractions(orderRepository);
    }

    /**
     * Given I Have 2 orders in the "DB"
     * When I request to get all the orders I should receive those same 2 orders
     * Then I validate the GET method return 200 OK
     * And I validate the GET method return the order list
     * Then I validate the GET method call the function "orderRepository.findAll()" once
     * And I validate the GET method does not call any more orderRepository functions
     */
    @Test
    @DisplayName("GET /api/v1/orders should return 200 OK with order list")
    void getAllOrders_WithTwoOrdersStored_ShouldReturnListOfOrders() throws Exception {
//        Generates random order data
        String id1 = UUID.randomUUID().toString().substring(0, 8);
        String id2 = UUID.randomUUID().toString().substring(0, 8);
        String item1 = "Item-" + UUID.randomUUID().toString().substring(0, 8);
        String item2 = "Item-" + UUID.randomUUID().toString().substring(0, 8);
        int quantity1 = ThreadLocalRandom.current().nextInt(1, 100);
        int quantity2 = ThreadLocalRandom.current().nextInt(1, 100);

//        Given I have 2 new orders in the "DB"
        when(orderRepository.findAll()).thenReturn(
                List.of(
                        new Order(id1, item1,quantity1, OrderStatus.PENDING),
                        new Order(id2, item2,quantity2, OrderStatus.PENDING)
                )
        );
//        When I perform the GET /api/v1/orders request
        mockMvc.perform(get("/api/v1/orders")
                        .accept(MediaType.APPLICATION_JSON))
//        Then I get the two existent orders
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
//                Validate the first element
                .andExpect(jsonPath("$[0].id").value(id1))
                .andExpect(jsonPath("$[0].item").value(item1))
                .andExpect(jsonPath("$[0].quantity").value(quantity1))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
//                Validate the second element
                .andExpect(jsonPath("$[1].id").value(id2))
                .andExpect(jsonPath("$[1].item").value(item2))
                .andExpect(jsonPath("$[1].quantity").value(quantity2))
                .andExpect(jsonPath("$[1].status").value("PENDING"));
//        And I validate the method ".findAll()" was only called once
        verify(orderRepository).findAll();
//        And I validate there were no other method calls
        verifyNoMoreInteractions(orderRepository);
    }

    /**
     * Given I Have 0 orders in the "DB"
     * When I request to create a new order
     * Then I validate the POST method return 201 Created
     * And I validate the POST method return the created order
     * Then I validate the POST method call the function "orderRepository.save()" once
     * And I validate the POST method does not call any more orderRepository functions
     */
    @Test
    @DisplayName("POST /api/v1/orders should return 201 CREATED with the order")
    void createOrders_NewOrder_ShouldReturnCreatedWithTheOrder() throws Exception {
//        Generates random order data
        String id = UUID.randomUUID().toString().substring(0, 8);
        String item = "Item-" + UUID.randomUUID().toString().substring(0, 8);
        int quantity = ThreadLocalRandom.current().nextInt(1, 100);
        Order order = new Order(id, item, quantity, OrderStatus.PENDING);

//        Given I have 0 new orders in the "DB"
        when(orderRepository.save(any(Order.class))).thenReturn(order);
//        When I perform the POST /api/v1/orders request
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateOrderRequest(item, quantity))))
//        Then I get the created order
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.item").value(item))
                .andExpect(jsonPath("$.quantity").value(quantity))
                .andExpect(jsonPath("$.status").value("PENDING"));
//        And I validate the method ".findAll()" was only called once
        verify(orderRepository).save(any(Order.class));
//        And I validate there were no other method calls
        verifyNoMoreInteractions(orderRepository);
    }

}
