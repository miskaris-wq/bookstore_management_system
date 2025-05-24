package com.bookstore.mapper.api;

public interface OneWayMapper<D, E> {
    D toDTO(E entity);
}
