package com.bookstore.dao.impl;

import com.bookstore.dao.api.PhysicStoreRepository;
import com.bookstore.dao.impl.utils.impl.PhysicStoreFinder;
import com.bookstore.datasourse.DataSource;
import com.bookstore.domain.Book;
import com.bookstore.domain.PhysicStore;
import com.bookstore.exceptions.PhysicStoreNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InMemoryPhysicStoreRepository implements PhysicStoreRepository {

    @Override
    public void add(PhysicStore store) {
        if (store == null) {
            throw new IllegalArgumentException("Store cannot be null");
        }
        DataSource.addStore(store.getId(), store);
    }

    @Override
    public boolean deleteById(UUID storeId) {
        if (!DataSource.getStores().containsKey(storeId)) {
            return false;
        }
        return DataSource.deleteStore(storeId);
    }

    @Override
    public List<PhysicStore> findById(UUID storeId) {
        return new PhysicStoreFinder().withId(storeId).find();
    }

    @Override
    public List<PhysicStore> findAll() {
        return new ArrayList<>(DataSource.getStores().values());
    }

    @Override
    public List<PhysicStore> findByAddress(String address) {
        return new PhysicStoreFinder().withAddress(address).find();
    }

    @Override
    public List<PhysicStore> findByName(String name) {
        return new PhysicStoreFinder().withName(name).find();
    }

    @Override
    public Map<Book, Integer> getInventory(UUID storeId) throws PhysicStoreNotFoundException {
        PhysicStore store = DataSource.getStores().get(storeId);
        if (store == null) {
            throw new PhysicStoreNotFoundException(storeId);
        }
        return store.getInventory();
    }

    @Override
    public PhysicStoreFinder newFinder() {
        return new PhysicStoreFinder();
    }
}
