package com.bookstore.dao.impl.utils.impl;

import com.bookstore.dao.impl.utils.api.EntityFinder;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractEntityFinder<T> implements EntityFinder<T> {
    protected Predicate<T> filter = entity -> true;

    @Override
    public List<T> find() {
        return getDataSource().stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    protected abstract List<T> getDataSource();

    protected void addFilter(Predicate<T> condition) {
        filter = filter.and(condition);
    }

    protected boolean containsIgnoreCase(String source, String target) {
        if (source == null || target == null) return false;
        return source.toLowerCase().contains(target.toLowerCase());
    }
}
