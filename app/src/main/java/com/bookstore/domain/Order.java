package com.bookstore.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Component;

public class Order {
    private final UUID id;               // Уникальный идентификатор
    private final String customerName;     // Имя клиента
    private final String deliveryAddress;  // Адрес доставки
    private final List<OrderItem> orderItems; // Список позиций
    private final LocalDateTime orderDate; // Дата создания заказа
    private OrderStatus status;            // Текущий статус

    public Order(String customerName, String deliveryAddress) {
        this.id = UUID.randomUUID();
        this.customerName = validateString(customerName, "Имя клиента");
        this.deliveryAddress = validateString(deliveryAddress, "Адрес доставки");
        this.orderItems = new ArrayList<>();
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.NEW;
    }

    public void addItem(OrderItem item) {
        Objects.requireNonNull(item, "Позиция заказа не может быть null");
        orderItems.add(item);
    }

    public void removeItem(OrderItem item) {
        orderItems.remove(item);
    }

    public BigDecimal calculateTotal() {
        return orderItems.stream()
                .map(OrderItem::getItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void processOrder() {
        if (status != OrderStatus.NEW) {
            throw new IllegalStateException("Заказ можно обрабатывать только из статуса NEW");
        }
        status = OrderStatus.PROCESSING;
    }

    public void shipOrder() {
        if (status != OrderStatus.PROCESSED) {
            throw new IllegalStateException("Заказ должен быть обработан перед отправкой");
        }
        status = OrderStatus.SHIPPED;
    }

    public void deliverOrder() {
        if (status != OrderStatus.SHIPPED) {
            throw new IllegalStateException("Заказ должен быть отправлен перед доставкой");
        }
        status = OrderStatus.DELIVERED;
    }

    public void cancelOrder() {
        if (status == OrderStatus.DELIVERED || status == OrderStatus.SHIPPED) {
            throw new IllegalStateException("Заказ не может быть отменен");
        }
        status = OrderStatus.CANCELLED;
    }

    // Геттеры
    public UUID getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getDeliveryAddress() { return deliveryAddress; }

    public List<OrderItem> getOrderItems() {
        return Collections.unmodifiableList(orderItems);
    }

    public LocalDateTime getOrderDate() { return orderDate; }
    public OrderStatus getStatus() { return status; }

    private String validateString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        }
        return value.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Заказ #%s\n", id));
        sb.append(String.format("Клиент: %s\n", customerName));
        sb.append(String.format("Адрес: %s\n", deliveryAddress));
        sb.append(String.format("Дата: %s\n", orderDate));
        sb.append(String.format("Статус: %s\n", status));
        sb.append("Позиции:\n");

        orderItems.forEach(item -> sb.append("  ").append(item).append("\n"));

        sb.append(String.format("Итого: %.2f руб.", calculateTotal()));
        return sb.toString();
    }
}
