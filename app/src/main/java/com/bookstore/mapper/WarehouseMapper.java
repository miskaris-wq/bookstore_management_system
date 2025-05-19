package com.bookstore.mapper;

import com.bookstore.domain.Warehouse;
import com.bookstore.dto.BookDTO;
import com.bookstore.dto.WarehouseDTO;

import java.util.Map;
import java.util.stream.Collectors;

public class WarehouseMapper {
    public static WarehouseDTO toDTO(Warehouse warehouse) {
        WarehouseDTO dto = new WarehouseDTO();
        dto.setId(warehouse.getId());
        dto.setAddress(warehouse.getAddress());
        Map<BookDTO, Integer> inventoryDto = warehouse.getInventory().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> BookMapper.toDTO(entry.getKey()),
                        Map.Entry::getValue
                ));
        dto.setInventory(inventoryDto);
        return dto;
    }
}
