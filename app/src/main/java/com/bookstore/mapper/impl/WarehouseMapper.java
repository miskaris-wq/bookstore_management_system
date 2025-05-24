package com.bookstore.mapper.impl;

import com.bookstore.domain.Warehouse;
import com.bookstore.dto.WarehouseDTO;
import com.bookstore.mapper.api.OneWayMapper;
import org.springframework.stereotype.Component;

@Component
public class WarehouseMapper implements OneWayMapper<WarehouseDTO, Warehouse> {

    private final InventoryMapperHelper inventoryMapper;

    public WarehouseMapper(InventoryMapperHelper inventoryMapper) {
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    public WarehouseDTO toDTO(Warehouse warehouse) {
        WarehouseDTO dto = new WarehouseDTO();
        dto.setId(warehouse.getId());
        dto.setAddress(warehouse.getAddress());
        dto.setInventory(inventoryMapper.toDTO(warehouse.getInventoryItems()));
        return dto;
    }
}
