package com.bookstore.dao.impl.utils.api;

import java.util.List;

public interface EntityFinder<T> {
    List<T> find();
}
