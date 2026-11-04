package com.brunomdleal.portfolio.orderservice.entity;

import com.brunomdleal.portfolio.orderservice.enums.OrderStatus;
import com.brunomdleal.portfolio.orderservice.model.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "item", nullable = false, length = 50)
    private String item;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private OrderStatus status;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private Instant createdAt;

    protected OrderEntity() {
//        Required by JPA/Hibernate reflection
    }

    public OrderEntity(UUID id, String item, int quantity, OrderStatus status) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.status = status;
    }

    //    Converts the model `Order` into a persistence entity
    public static OrderEntity fromDomain(Order order) {
        UUID id;
        try {
            id = order.id() != null ? UUID.fromString(order.id()) : UUID.randomUUID();
        } catch (IllegalArgumentException e) {
            id = UUID.randomUUID();
        }
        return new OrderEntity(
                id,
                order.item(),
                order.quantity(),
                order.status()
        );
    }

    //    Converts a persistence entity into the model `Order`
    public Order toDomain() {
        return new Order(
                this.id.toString(),
                this.item,
                this.quantity,
                this.status
        );
    }

}
