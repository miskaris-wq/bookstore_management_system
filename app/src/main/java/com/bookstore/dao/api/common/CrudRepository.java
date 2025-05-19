package com.bookstore.dao.api.common;

import java.util.List;
import java.util.UUID;

public interface CrudRepository<T> {
    void add(T entity);
    boolean deleteById(UUID id);
    List<T> findById(UUID id);
    List<T> findAll();
}
