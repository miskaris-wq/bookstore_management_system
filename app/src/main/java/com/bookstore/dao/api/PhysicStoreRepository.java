package com.bookstore.dao.api;

import com.bookstore.dao.api.common.SearchableRepository;
import com.bookstore.dao.impl.utils.impl.PhysicStoreFinder;
import com.bookstore.domain.Book;
import com.bookstore.domain.PhysicStore;
import com.bookstore.exceptions.PhysicStoreNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PhysicStoreRepository extends SearchableRepository<PhysicStore, PhysicStoreFinder> {
    List<PhysicStore> findByAddress(String address);
    List<PhysicStore> findByName(String name);
    Map<Book, Integer> getInventory(UUID storeId) throws PhysicStoreNotFoundException;
}
