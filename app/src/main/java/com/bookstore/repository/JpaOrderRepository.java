package com.bookstore.repository;

import com.bookstore.domain.Order;
import com.bookstore.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByCustomerNameContainingIgnoreCase(String customerName);
    List<Order> findByDeliveryAddressContainingIgnoreCase(String deliveryAddress);
    List<Order> findByOrderDate(LocalDateTime orderDate);
    List<Order> findByStatus(OrderStatus status);
}
