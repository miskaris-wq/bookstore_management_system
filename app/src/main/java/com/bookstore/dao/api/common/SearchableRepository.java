package com.bookstore.dao.api.common;

public interface SearchableRepository<T, F> extends CrudRepository<T> {
    F newFinder();
}

