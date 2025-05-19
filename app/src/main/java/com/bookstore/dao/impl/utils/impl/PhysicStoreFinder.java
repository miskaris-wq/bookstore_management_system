package com.bookstore.dao.impl.utils.impl;

import com.bookstore.datasourse.DataSource;
import com.bookstore.domain.PhysicStore;
import java.util.List;
import java.util.UUID;

public class PhysicStoreFinder extends AbstractEntityFinder<PhysicStore> {
    @Override
    protected List<PhysicStore> getDataSource() {
        return DataSource.getStores().values().stream().toList();
    }

    public PhysicStoreFinder withId(UUID id) {
        addFilter(store -> id == null || store.getId().equals(id));
        return this;
    }

    public PhysicStoreFinder withName(String name) {
        addFilter(store -> name == null || containsIgnoreCase(store.getName(), name));
        return this;
    }

    public PhysicStoreFinder withAddress(String address) {
        addFilter(store -> address == null || containsIgnoreCase(store.getAddress(), address));
        return this;
    }
}
