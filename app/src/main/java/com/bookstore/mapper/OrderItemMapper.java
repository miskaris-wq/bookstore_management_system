package com.bookstore.mapper;

import com.bookstore.domain.OrderItem;
import com.bookstore.dto.OrderItemDTO;

public class OrderItemMapper {
    public static OrderItemDTO toDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setBook(BookMapper.toDTO(item.getBook()));
        dto.setQuantity(item.getQuantity());
        dto.setItemTotal(item.getItemTotal());
        return dto;
    }
}
