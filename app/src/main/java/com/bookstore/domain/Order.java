package com.bookstore.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String deliveryAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;


    public Order() {
    }

    public Order(String customerName, String deliveryAddress) {
        this.customerName = validateString(customerName, "Имя клиента");
        this.deliveryAddress = validateString(deliveryAddress, "Адрес доставки");
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.NEW;
    }

    public UUID getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = validateString(customerName, "Имя клиента");
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = validateString(deliveryAddress, "Адрес доставки");
    }

    public List<OrderItem> getOrderItems() {
        return Collections.unmodifiableList(orderItems);
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }


    private String validateString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        return value.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(getId(), order.getId()) && Objects.equals(getCustomerName(), order.getCustomerName()) && Objects.equals(getDeliveryAddress(), order.getDeliveryAddress()) && Objects.equals(getOrderItems(), order.getOrderItems()) && Objects.equals(getOrderDate(), order.getOrderDate()) && getStatus() == order.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCustomerName(), getDeliveryAddress(), getOrderItems(), getOrderDate(), getStatus());
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Order #%s\n", id));
        sb.append(String.format("Customer: %s\n", customerName));
        sb.append(String.format("Address: %s\n", deliveryAddress));
        sb.append(String.format("Date: %s\n", orderDate));
        sb.append(String.format("Status: %s\n", status));
        sb.append("Items:\n");
        orderItems.forEach(item -> sb.append("  ").append(item).append("\n"));
        return sb.toString();
    }
}
