package com.bookstore.mapper.api;

public interface GenericMapper<D, E> {
    D toDTO(E entity);
    E fromDTO(D dto);
}
