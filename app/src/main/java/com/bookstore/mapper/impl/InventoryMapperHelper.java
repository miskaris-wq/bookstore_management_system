package com.bookstore.mapper.impl;

import com.bookstore.domain.InventoryItem;
import com.bookstore.dto.BookDTO;
import com.bookstore.mapper.api.OneWayMapper;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InventoryMapperHelper implements OneWayMapper<Map<BookDTO, Integer>, Set<InventoryItem>> {

    private final BookMapper bookMapper;

    public InventoryMapperHelper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Override
    public Map<BookDTO, Integer> toDTO(Set<InventoryItem> items) {
        return items.stream()
                .collect(Collectors.toMap(
                        item -> bookMapper.toDTO(item.getBook()),
                        InventoryItem::getQuantity
                ));
    }
}
