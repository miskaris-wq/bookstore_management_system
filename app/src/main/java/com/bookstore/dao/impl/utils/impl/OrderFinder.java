package com.bookstore.dao.impl.utils.impl;

import com.bookstore.datasourse.DataSource;
import com.bookstore.domain.Order;
import com.bookstore.domain.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderFinder extends AbstractEntityFinder<Order> {
    @Override
    protected List<Order> getDataSource() {
        return DataSource.getOrders().values().stream().toList();
    }

    public OrderFinder withId(UUID id) {
        addFilter(order -> id == null || order.getId().equals(id));
        return this;
    }

    public OrderFinder withCustomerName(String customerName) {
        addFilter(order -> customerName == null || containsIgnoreCase(order.getCustomerName(), customerName));
        return this;
    }

    public OrderFinder withDeliveryAddress(String deliveryAddress) {
        addFilter(order -> deliveryAddress == null || containsIgnoreCase(order.getDeliveryAddress(), deliveryAddress));
        return this;
    }

    public OrderFinder withOrderDate(LocalDateTime orderDate) {
        addFilter(order -> orderDate == null || isSameDay(order.getOrderDate(), orderDate));
        return this;
    }

    public OrderFinder withStatus(OrderStatus status) {
        addFilter(order -> status == null || order.getStatus() == status);
        return this;
    }

    private boolean isSameDay(LocalDateTime date1, LocalDateTime date2) {
        return date1.toLocalDate().equals(date2.toLocalDate());
    }
}
