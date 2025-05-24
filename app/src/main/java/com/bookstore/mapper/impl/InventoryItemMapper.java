package com.bookstore.mapper.impl;

import com.bookstore.domain.InventoryItem;
import com.bookstore.domain.PhysicStore;
import com.bookstore.domain.Warehouse;
import com.bookstore.dto.InventoryItemDTO;
import com.bookstore.mapper.api.GenericMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryItemMapper implements GenericMapper<InventoryItemDTO, InventoryItem> {

    private final BookMapper bookMapper;
    private final PhysicStoreMapper physicStoreMapper;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public InventoryItemMapper(BookMapper bookMapper,
                               PhysicStoreMapper physicStoreMapper,
                               WarehouseMapper warehouseMapper) {
        this.bookMapper = bookMapper;
        this.physicStoreMapper = physicStoreMapper;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public InventoryItemDTO toDTO(InventoryItem item) {
        if (item == null) {
            return null;
        }

        InventoryItemDTO dto = new InventoryItemDTO();
        dto.setId(item.getId());
        dto.setBook(bookMapper.toDTO(item.getBook()));
        dto.setQuantity(item.getQuantity());

        if (item.getInventoryHolder() instanceof PhysicStore) {
            dto.setInventoryHolder(physicStoreMapper.toDTO((PhysicStore) item.getInventoryHolder()));
        } else if (item.getInventoryHolder() instanceof Warehouse) {
            dto.setInventoryHolder(warehouseMapper.toDTO((Warehouse) item.getInventoryHolder()));
        }

        return dto;
    }

    @Override
    public InventoryItem fromDTO(InventoryItemDTO dto) {
        if (dto == null) {
            return null;
        }

        InventoryItem item = new InventoryItem();
        item.setId(dto.getId());
        item.setBook(bookMapper.fromDTO(dto.getBook()));
        item.setQuantity(dto.getQuantity());

        return item;
    }
}
