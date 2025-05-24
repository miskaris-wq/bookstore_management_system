package com.bookstore.mapper.impl;

import com.bookstore.domain.OrderItem;
import com.bookstore.dto.OrderItemDTO;
import com.bookstore.mapper.api.GenericMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper implements GenericMapper<OrderItemDTO, OrderItem> {
    private final BookMapper bookMapper;

    public OrderItemMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Override
    public OrderItemDTO toDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setBook(bookMapper.toDTO(item.getBook()));
        dto.setQuantity(item.getQuantity());
        return dto;
    }

    @Override
    public OrderItem fromDTO(OrderItemDTO dto) {
        OrderItem item = new OrderItem();
        item.setBook(bookMapper.fromDTO(dto.getBook()));
        item.setQuantity(dto.getQuantity());
        return item;
    }

}
