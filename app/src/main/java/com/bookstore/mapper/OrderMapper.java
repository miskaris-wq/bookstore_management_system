package com.bookstore.mapper;

import com.bookstore.domain.Order;
import com.bookstore.dto.OrderDTO;

import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setCustomerName(order.getCustomerName());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus().name());
        dto.setOrderItems(order.getOrderItems().stream()
                .map(OrderItemMapper::toDTO)
                .collect(Collectors.toList()));
        dto.setTotal(order.calculateTotal());
        return dto;
    }
}
