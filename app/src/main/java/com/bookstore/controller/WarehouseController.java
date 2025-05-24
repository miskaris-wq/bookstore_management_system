package com.bookstore.controller;

import com.bookstore.dto.BookDTO;
import com.bookstore.exceptions.InsufficientStockException;
import com.bookstore.exceptions.WarehouseNotFoundException;
import com.bookstore.mapper.impl.BookMapper;
import com.bookstore.services.WarehouseService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController extends AbstractInventoryController {
    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService, BookMapper bookMapper, WarehouseService warehouseService1) {
        super( bookMapper);
        this.warehouseService = warehouseService1;
    }

    @Override
    public Map<BookDTO, Integer> getInventory(@PathVariable UUID id) throws WarehouseNotFoundException {
        return convertInventory(warehouseService.getInventory(id));
    }

    @Override
    public void addBook(@PathVariable UUID id, @RequestBody BookDTO dto, @RequestParam int quantity) throws WarehouseNotFoundException {
        warehouseService.addBook(id, bookMapper.fromDTO(dto), quantity);
    }

    @Override
    public void removeBook(@PathVariable UUID id, @RequestBody BookDTO dto, @RequestParam int quantity) throws InsufficientStockException, WarehouseNotFoundException {
        warehouseService.removeBook(id, bookMapper.fromDTO(dto), quantity);
    }
}
