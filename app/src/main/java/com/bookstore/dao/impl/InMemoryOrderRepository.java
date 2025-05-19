package com.bookstore.dao.impl;

import com.bookstore.dao.api.OrderRepository;
import com.bookstore.dao.impl.utils.impl.OrderFinder;
import com.bookstore.datasourse.DataSource;
import com.bookstore.domain.Order;
import com.bookstore.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryOrderRepository implements OrderRepository {

    @Override
    public void add(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        DataSource.addOrder(order.getId(), order);
    }

    @Override
    public boolean deleteById(UUID orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        return DataSource.deleteOrder(orderId);
    }

    @Override
    public List<Order> findById(UUID id) {
        return new OrderFinder().withId(id).find();
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(DataSource.getOrders().values());
    }

    @Override
    public List<Order> findByCustomer(String customer) {
        return new OrderFinder().withCustomerName(customer).find();
    }

    @Override
    public List<Order> findByDeliveryAddress(String deliveryAddress) {
        return new OrderFinder().withDeliveryAddress(deliveryAddress).find();
    }

    @Override
    public List<Order> findByOrderDate(LocalDateTime orderDate) {
        return new OrderFinder().withOrderDate(orderDate).find();
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return new OrderFinder().withStatus(status).find();
    }

    @Override
    public OrderFinder newFinder() {
        return new OrderFinder();
    }
}
