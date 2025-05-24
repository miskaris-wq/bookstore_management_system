package com.bookstore.mapper.impl;

import com.bookstore.domain.Order;
import com.bookstore.domain.OrderStatus;
import com.bookstore.dto.OrderDTO;
import com.bookstore.mapper.api.GenericMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper implements GenericMapper<OrderDTO, Order> {

    private final OrderItemMapper orderItemMapper;

    public OrderMapper(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    public OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setCustomerName(order.getCustomerName());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus().name());
        dto.setOrderItems(order.getOrderItems().stream()
                .map(orderItemMapper::toDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    @Override
    public Order fromDTO(OrderDTO dto) {
        Order order = new Order();
        order.setCustomerName(dto.getCustomerName());
        order.setDeliveryAddress(dto.getDeliveryAddress());
        order.setOrderDate(dto.getOrderDate());
        order.setStatus(OrderStatus.valueOf(dto.getStatus()));
        order.setOrderItems(dto.getOrderItems().stream()
                .map(orderItemMapper::fromDTO)
                .collect(Collectors.toList()));
        return order;
    }
}
