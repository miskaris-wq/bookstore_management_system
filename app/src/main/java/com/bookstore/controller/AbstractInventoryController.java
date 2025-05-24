package com.bookstore.controller;

import com.bookstore.domain.Book;
import com.bookstore.dto.BookDTO;
import com.bookstore.mapper.impl.BookMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class AbstractInventoryController{
    protected final BookMapper bookMapper;

    private static final String ROLE_MANAGER = "MANAGER";
    private static final String ROLE_EMPLOYEE = "EMPLOYEE";

    protected static final String MANAGER_ONLY = "hasRole('" + ROLE_MANAGER + "')";
    protected static final String MANAGER_OR_EMPLOYEE = "hasAnyRole('" + ROLE_MANAGER + "','" + ROLE_EMPLOYEE + "')";

    protected AbstractInventoryController( BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @PreAuthorize(MANAGER_OR_EMPLOYEE)
    @GetMapping("/{id}/inventory")
    public abstract Map<BookDTO, Integer> getInventory(@PathVariable UUID id) throws Exception;

    @PreAuthorize(MANAGER_ONLY)
    @PostMapping("/{id}/add")
    public abstract void addBook(@PathVariable UUID id, @RequestBody BookDTO dto, @RequestParam int quantity) throws Exception;

    @PreAuthorize(MANAGER_ONLY)
    @PostMapping("/{id}/remove")
    public abstract void removeBook(@PathVariable UUID id, @RequestBody BookDTO dto, @RequestParam int quantity) throws Exception;

    protected Map<BookDTO, Integer> convertInventory(Map<Book, Integer> inventory) {
        return inventory.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> bookMapper.toDTO(e.getKey()),
                        Map.Entry::getValue
                ));
    }
}
