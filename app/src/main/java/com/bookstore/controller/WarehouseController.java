package com.bookstore.controller;

import com.bookstore.domain.Book;
import com.bookstore.dto.BookDTO;
import com.bookstore.exceptions.InsufficientStockException;
import com.bookstore.exceptions.WarehouseNotFoundException;
import com.bookstore.mapper.BookMapper;
import com.bookstore.services.WarehouseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {
    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PreAuthorize("hasRole('WAREHOUSE')")
    @GetMapping("/{id}/inventory")
    public Map<BookDTO, Integer> getInventory(@PathVariable UUID id) throws WarehouseNotFoundException {
        return warehouseService.getInventory(id).entrySet().stream()
                .collect(Collectors.toMap(
                        e -> BookMapper.toDTO(e.getKey()),
                        Map.Entry::getValue
                ));
    }
    @PreAuthorize("hasRole('WAREHOUSE')")
    @PostMapping("/{id}/add")
    public void addBook(@PathVariable UUID id, @RequestBody BookDTO bookDTO, @RequestParam int quantity) throws WarehouseNotFoundException {
        Book book = BookMapper.fromDTO(bookDTO);
        warehouseService.addBook(id, book, quantity);
    }
    @PreAuthorize("hasRole('WAREHOUSE')")
    @PostMapping("/{id}/remove")
    public void removeBook(@PathVariable UUID id, @RequestBody BookDTO bookDto, @RequestParam int quantity) throws InsufficientStockException, WarehouseNotFoundException {
        Book book = BookMapper.fromDTO(bookDto);
        warehouseService.removeBook(id, book, quantity);
    }
}
