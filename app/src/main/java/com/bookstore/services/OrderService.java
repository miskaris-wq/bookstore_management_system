package com.bookstore.services;

import com.bookstore.dao.api.OrderRepository;
import com.bookstore.domain.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = Objects.requireNonNull(orderRepository);
    }
    @PreAuthorize("#customer == authentication.name or hasAnyRole('ADMIN', 'STAFF')")
    public List<Order> findOrdersByCustomer(String customer) {
        validateStringParam(customer, "Customer name");
        return orderRepository.findByCustomer(customer);
    }
    @PreAuthorize("@orderSecurityService.canAccessOrder(authentication, #id) or hasRole('ADMIN')")
    public Optional<Order> findOrderById(UUID id) {
        Objects.requireNonNull(id, "Order ID cannot be null");
        List<Order> orders = orderRepository.findById(id);
        return orders.isEmpty() ? Optional.empty() : Optional.of(orders.getFirst());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public void saveOrder(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");
        orderRepository.add(order);
    }

    private void validateStringParam(String param, String paramName) {
        if (param == null || param.trim().isEmpty()) {
            throw new IllegalArgumentException(paramName + " cannot be empty");
        }
    }
}
