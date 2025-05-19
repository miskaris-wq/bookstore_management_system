package com.bookstore.dao.impl.utils.impl;

import com.bookstore.datasourse.DataSource;
import com.bookstore.domain.Warehouse;
import java.util.List;
import java.util.UUID;

public class WarehouseFinder extends AbstractEntityFinder<Warehouse> {
    @Override
    protected List<Warehouse> getDataSource() {
        return DataSource.getWarehouses().values().stream().toList();
    }

    public WarehouseFinder withId(UUID id) {
        addFilter(warehouse -> id == null || warehouse.getId().equals(id));
        return this;
    }

    public WarehouseFinder withAddress(String address) {
        addFilter(warehouse -> address == null || containsIgnoreCase(warehouse.getAddress(), address));
        return this;
    }
}
