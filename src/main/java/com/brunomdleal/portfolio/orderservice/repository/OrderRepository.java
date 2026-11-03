package com.brunomdleal.portfolio.orderservice.repository;

import com.brunomdleal.portfolio.orderservice.entity.OrderEntity;
import com.brunomdleal.portfolio.orderservice.model.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderRepository {
    private final SpringDataOrderRepository jpaRepository;
//    private final ConcurrentHashMap<String, Order> orders = new ConcurrentHashMap<>();


    public OrderRepository(SpringDataOrderRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    public Order save(Order order) {
        return jpaRepository.save(
                OrderEntity.fromDomain(order)
        ).toDomain();
    }

    public List<Order> findAll() {
        return jpaRepository.findAll().stream().map(OrderEntity::toDomain).toList();
    }

    public Optional<Order> findById(String id) {
        try {
            return jpaRepository.findById(UUID.fromString(id)).map(OrderEntity::toDomain);
        } catch (IllegalArgumentException missingUUID) {
            return Optional.empty();
        }
    }
}
