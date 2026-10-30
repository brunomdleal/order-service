package com.brunomdleal.portfolio.orderservice.controller;

import com.brunomdleal.portfolio.orderservice.dto.CreateOrderRequest;
import com.brunomdleal.portfolio.orderservice.model.Order;
import com.brunomdleal.portfolio.orderservice.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderRepository orderRepository;

    // Constructor injection (Spring automatically injects OrderRepository here)
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        Order order = orderRepository.save(
                new Order(request.item(), request.quantity())
        );
//        Gets the URL to get the new order
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(order.id())
                .toUri();
/*
        .created -> Add status code of 201
        location -> Add Location HTTP header
        .body -> Add response body
 */
        return ResponseEntity.created(location).body(order);
    }

}
