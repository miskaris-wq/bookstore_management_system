package com.bookstore.controller;

import com.bookstore.dto.BookDTO;
import com.bookstore.exceptions.InsufficientStockException;
import com.bookstore.exceptions.PhysicStoreNotFoundException;
import com.bookstore.mapper.BookMapper;
import com.bookstore.services.PhysicStoreService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stores")
public class PhysicStoreController {
    private final PhysicStoreService storeService;

    public PhysicStoreController(PhysicStoreService storeService) {
        this.storeService = storeService;
    }

    @PreAuthorize("hasRole('STORE')")
    @GetMapping("/{id}/inventory")
    public Map<BookDTO, Integer> getInventory(@PathVariable UUID id) {
        return storeService.getInventory(id).entrySet().stream()
                .collect(Collectors.toMap(
                        e -> BookMapper.toDTO(e.getKey()),
                        Map.Entry::getValue
                ));
    }
    @PreAuthorize("hasRole('STORE')")
    @PostMapping("/{id}/add")
    public void addBook(@PathVariable UUID id, @RequestBody BookDTO dto, @RequestParam int quantity) throws PhysicStoreNotFoundException {
        storeService.addBook(id, BookMapper.fromDTO(dto), quantity);
    }
    @PreAuthorize("hasRole('STORE')")
    @PostMapping("/{id}/remove")
    public void removeBook(@PathVariable UUID id, @RequestBody BookDTO dto, @RequestParam int quantity) throws PhysicStoreNotFoundException, InsufficientStockException {
        storeService.removeBook(id, BookMapper.fromDTO(dto), quantity);
    }
}
