package com.bookstore.mapper;

import com.bookstore.domain.PhysicStore;
import com.bookstore.dto.BookDTO;
import com.bookstore.dto.PhysicStoreDTO;

import java.util.Map;
import java.util.stream.Collectors;

public class PhysicStoreMapper {
    public static PhysicStoreDTO toDTO(PhysicStore store) {
        PhysicStoreDTO dto = new PhysicStoreDTO();
        dto.setId(store.getId());
        dto.setName(store.getName());
        dto.setAddress(store.getAddress());
        Map<BookDTO, Integer> inventoryDto = store.getInventory().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> BookMapper.toDTO(entry.getKey()),
                        Map.Entry::getValue
                ));
        dto.setInventory(inventoryDto);
        return dto;
    }
}
