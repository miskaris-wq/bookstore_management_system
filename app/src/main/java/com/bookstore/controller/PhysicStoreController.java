package com.bookstore.controller;

import com.bookstore.dto.BookDTO;
import com.bookstore.exceptions.InsufficientStockException;
import com.bookstore.exceptions.PhysicStoreNotFoundException;
import com.bookstore.mapper.impl.BookMapper;
import com.bookstore.services.PhysicStoreService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/stores")
public class PhysicStoreController extends AbstractInventoryController {
    private final PhysicStoreService physicStoreService;
    public PhysicStoreController(BookMapper bookMapper, PhysicStoreService service) {
        super(bookMapper);
        this.physicStoreService = service;
    }

    @Override
    public Map<BookDTO, Integer> getInventory(@PathVariable UUID id) throws PhysicStoreNotFoundException {
        return convertInventory(physicStoreService.getInventory(id));
    }

    @Override
    public void addBook(@PathVariable UUID id, @RequestBody BookDTO dto, @RequestParam int quantity) throws PhysicStoreNotFoundException {
        physicStoreService.addBook(id, bookMapper.fromDTO(dto), quantity);
    }

    @Override
    public void removeBook(@PathVariable UUID id, @RequestBody BookDTO dto, @RequestParam int quantity) throws PhysicStoreNotFoundException, InsufficientStockException {
        physicStoreService.removeBook(id, bookMapper.fromDTO(dto), quantity);
    }
}
