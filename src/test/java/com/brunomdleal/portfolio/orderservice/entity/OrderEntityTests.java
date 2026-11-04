package com.brunomdleal.portfolio.orderservice.entity;

import com.brunomdleal.portfolio.orderservice.enums.OrderStatus;
import com.brunomdleal.portfolio.orderservice.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.brunomdleal.portfolio.orderservice.utils.OrderCreator.generateOrder;
import static com.brunomdleal.portfolio.orderservice.utils.OrderCreator.generateOrderName;
import static org.junit.jupiter.api.Assertions.*;

public class OrderEntityTests {

    @Test
    @DisplayName("Protected constructor instantiates successfully for JPA reflection")
    public void noArgConstructor_InstantiatesSuccessfully() {
        OrderEntity entity = new OrderEntity();
        assertNotNull(entity);
    }

    @Test
    @DisplayName("Validate is possible to create a new OrderEntity")
    public void createNewOrderEntity_WithSuccess() {
        UUID id = UUID.randomUUID();
        String orderItem = "Order to check if is possible to create a new OrderEntity";
        int orderQuantity = 1;
        OrderStatus orderStatus = OrderStatus.CONFIRMED;
        OrderEntity orderEntity = new OrderEntity(
                id,
                orderItem,
                orderQuantity,
                orderStatus
        );
        assertEquals(id, orderEntity.getId());
        assertEquals(orderItem, orderEntity.getItem());
        assertEquals(orderQuantity, orderEntity.getQuantity());
        assertEquals(orderStatus, orderEntity.getStatus());
    }

    @Test
    @DisplayName("Validate fromDomain method preserves the order data")
    public void fromDomain_WithExistentID_PreservesOrderData() {
        UUID id = UUID.randomUUID();
        String orderItem = "Order to check if the fromDomain method preserves the order data";
        int orderQuantity = 1;

        OrderEntity orderEntity = OrderEntity.fromDomain(
                generateOrder(
                        id.toString(),
                        orderQuantity,
                        orderItem
                )
        );
        assertEquals(id, orderEntity.getId());
        assertEquals(generateOrderName(orderQuantity, orderItem), orderEntity.getItem());
        assertEquals(orderQuantity, orderEntity.getQuantity());
        assertEquals(OrderStatus.CONFIRMED, orderEntity.getStatus());
    }

    @Test
    @DisplayName("Validate fromDomain method generates a new ID when the given one is too long")
    public void fromDomain_WithNullID_GeneratesOne() {
        String orderItem = "Order to check if the fromDomain generates a new ID when the given one is too long";
        int orderQuantity = 1;

        OrderEntity orderEntity = OrderEntity.fromDomain(
                generateOrder(
                        null,
                        orderQuantity,
                        orderItem
                )
        );
        assertNotNull(orderEntity.getId());
    }

    @Test
    @DisplayName("Validate toDomain method converts an OrderEntity into the model Order")
    public void toDomain_WithValidOrderEntity_ConvertsToOrder() {
        Order originalOrder = generateOrder(
                UUID.randomUUID().toString(),
                1,
                "Order to check if the fromDomain method converts an OrderEntity into the model Order"
        );
        assertEquals(originalOrder, OrderEntity.fromDomain(originalOrder).toDomain());
    }

}
