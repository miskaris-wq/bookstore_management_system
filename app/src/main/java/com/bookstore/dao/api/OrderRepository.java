package com.bookstore.dao.api;

import com.bookstore.dao.api.common.SearchableRepository;
import com.bookstore.dao.impl.utils.impl.OrderFinder;
import com.bookstore.domain.Order;
import com.bookstore.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends SearchableRepository<Order, OrderFinder> {
    List<Order> findByCustomer(String customer);
    List<Order> findByDeliveryAddress(String address);
    List<Order> findByOrderDate(LocalDateTime date);
    List<Order> findByStatus(OrderStatus status);
}
