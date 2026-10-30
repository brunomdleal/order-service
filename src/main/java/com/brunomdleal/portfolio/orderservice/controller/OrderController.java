package com.brunomdleal.portfolio.orderservice.controller;

import com.brunomdleal.portfolio.orderservice.repository.OrderRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderRepository orderRepository;

    // Constructor injection (Spring automatically injects OrderRepository here)
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

}
