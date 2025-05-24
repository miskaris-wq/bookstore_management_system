package com.bookstore.mapper.impl;

import com.bookstore.domain.PhysicStore;
import com.bookstore.dto.PhysicStoreDTO;
import com.bookstore.mapper.api.OneWayMapper;
import org.springframework.stereotype.Component;

@Component
public class PhysicStoreMapper implements OneWayMapper<PhysicStoreDTO, PhysicStore> {

    private final InventoryMapperHelper inventoryMapper;

    public PhysicStoreMapper(InventoryMapperHelper inventoryMapper) {
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    public PhysicStoreDTO toDTO(PhysicStore store) {
        PhysicStoreDTO dto = new PhysicStoreDTO();
        dto.setId(store.getId());
        dto.setName(store.getName());
        dto.setAddress(store.getAddress());
        dto.setInventory(inventoryMapper.toDTO(store.getInventoryItems()));
        return dto;
    }
}
