package com.brunomdleal.portfolio.orderservice.repository;

import com.brunomdleal.portfolio.orderservice.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataOrderRepository extends JpaRepository<OrderEntity, UUID> {
    // Spring Data generates save, findById, findAll, and deleteById automatically!
}
