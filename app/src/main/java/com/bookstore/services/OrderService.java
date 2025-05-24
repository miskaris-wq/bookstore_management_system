package com.bookstore.services;

import com.bookstore.domain.Order;
import com.bookstore.domain.OrderItem;
import com.bookstore.domain.OrderStatus;
import com.bookstore.repository.JpaOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final JpaOrderRepository orderRepository;

    public OrderService(JpaOrderRepository orderRepository) {
        this.orderRepository = Objects.requireNonNull(orderRepository);
    }

    public List<Order> findOrdersByCustomer(String customer) {
        validateStringParam(customer, "Customer name");
        return orderRepository.findByCustomerNameContainingIgnoreCase(customer);
    }

    public Optional<Order> findOrderById(UUID id) {
        Objects.requireNonNull(id, "Order ID cannot be null");
        return orderRepository.findById(id);
    }

    @Transactional
    public Order saveOrder(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");
        return orderRepository.save(order);
    }

    public BigDecimal calculateOrderTotal(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");
        return order.getOrderItems().stream()
                .map(this::calculateItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public void processOrder(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");
        if (order.getStatus() != OrderStatus.NEW) {
            throw new IllegalStateException("An order can only be processed from the NEW status");
        }
        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);
    }

    @Transactional
    public void shipOrder(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");
        if (order.getStatus() != OrderStatus.PROCESSED) {
            throw new IllegalStateException("The order must be PROCESSED before shipping.");
        }
        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
    }

    @Transactional
    public void deliverOrder(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");
        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new IllegalStateException("The order must be SHIPPED before delivery");
        }
        order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");
        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.SHIPPED) {
            throw new IllegalStateException("Order cannot be CANCELLED after shipping");
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    public BigDecimal calculateItemTotal(OrderItem item) {
        return item.getBook().getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));
    }

    private void validateStringParam(String param, String paramName) {
        if (param == null || param.trim().isEmpty()) {
            throw new IllegalArgumentException(paramName + " cannot be empty");
        }
    }
}
