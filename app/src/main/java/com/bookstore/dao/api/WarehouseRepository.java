package com.bookstore.dao.api;

import com.bookstore.dao.api.common.SearchableRepository;
import com.bookstore.dao.impl.utils.impl.WarehouseFinder;
import com.bookstore.domain.Book;
import com.bookstore.domain.Warehouse;
import com.bookstore.exceptions.WarehouseNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface WarehouseRepository extends SearchableRepository<Warehouse, WarehouseFinder> {

    boolean deleteById(UUID id);

    List<Warehouse> findAll();
    List<Warehouse> findById(UUID id);
    List<Warehouse> findByAddress(String address);

    Map<Book, Integer> getInventory(UUID warehouseId) throws WarehouseNotFoundException;
}
