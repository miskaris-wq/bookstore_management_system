package com.bookstore.dao.impl;

import com.bookstore.dao.api.WarehouseRepository;
import com.bookstore.dao.impl.utils.impl.WarehouseFinder;
import com.bookstore.datasourse.DataSource;
import com.bookstore.domain.Book;
import com.bookstore.domain.Warehouse;
import com.bookstore.exceptions.WarehouseNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InMemoryWarehouseRepository implements WarehouseRepository {

    @Override
    public List<Warehouse> findAll() {
        return new ArrayList<>(DataSource.getWarehouses().values());
    }

    @Override
    public List<Warehouse> findById(UUID warehouseId) {
        return new WarehouseFinder().withId(warehouseId).find();
    }

    @Override
    public List<Warehouse> findByAddress(String address) {
        return new WarehouseFinder().withAddress(address).find();
    }

    @Override
    public void add(Warehouse warehouse) {
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse cannot be null");
        }
        DataSource.addWarehouse(warehouse.getId(), warehouse);
    }

    @Override
    public boolean deleteById(UUID warehouseId) {
        if (!DataSource.getWarehouses().containsKey(warehouseId)) {
            return false;
        }
        return DataSource.deleteWarehouse(warehouseId);
    }

    @Override
    public Map<Book, Integer> getInventory(UUID warehouseId) throws WarehouseNotFoundException {
        Warehouse warehouse = DataSource.getWarehouses().get(warehouseId);
        if (warehouse == null) {
            throw new WarehouseNotFoundException(warehouseId);
        }
        return warehouse.getInventory();
    }

    @Override
    public WarehouseFinder newFinder() {
        return new WarehouseFinder();
    }
}
