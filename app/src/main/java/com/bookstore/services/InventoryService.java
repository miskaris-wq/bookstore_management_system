package com.bookstore.services;

import com.bookstore.domain.*;
import com.bookstore.exceptions.InsufficientStockException;
import com.bookstore.repository.JpaBookRepository;
import com.bookstore.repository.JpaPhysicStoreRepository;
import com.bookstore.repository.JpaWarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class InventoryService {

    private final JpaWarehouseRepository warehouseRepository;
    private final JpaPhysicStoreRepository storeRepository;
    private final JpaBookRepository bookRepository;
    private final WarehouseService warehouseService;
    private final PhysicStoreService storeService;

    public InventoryService(JpaWarehouseRepository warehouseRepository,
                            JpaPhysicStoreRepository storeRepository,
                            JpaBookRepository bookRepository,
                            WarehouseService warehouseService,
                            PhysicStoreService storeService) {
        this.warehouseRepository = Objects.requireNonNull(warehouseRepository);
        this.storeRepository = Objects.requireNonNull(storeRepository);
        this.bookRepository = Objects.requireNonNull(bookRepository);
        this.warehouseService = Objects.requireNonNull(warehouseService);
        this.storeService = Objects.requireNonNull(storeService);
    }

    @Transactional(readOnly = true)
    public int getTotalStock(Book book) {
        validateBook(book);

        int warehouseStock = warehouseRepository.findAll().stream()
                .mapToInt(w -> getQuantityFromHolder(w, book))
                .sum();

        int storeStock = storeRepository.findAll().stream()
                .mapToInt(s -> getQuantityFromHolder(s, book))
                .sum();

        return warehouseStock + storeStock;
    }

    @Transactional
    public void redistributeStock(Book book, int quantity) throws InsufficientStockException {
        validateBook(book);

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        if (!bookRepository.existsById(book.getId())) {
            throw new IllegalArgumentException("Book does not exist");
        }

        int totalAvailable = getTotalStock(book);
        if (totalAvailable < quantity) {
            throw new InsufficientStockException(book.getTitle(), totalAvailable, quantity);
        }

        int remaining = quantity;

        for (Warehouse warehouse : warehouseRepository.findAll()) {
            if (remaining == 0) break;

            int available = getQuantityFromHolder(warehouse, book);
            int toRemove = Math.min(available, remaining);
            if (toRemove > 0) {
                warehouseService.removeBook(warehouse.getId(), book, toRemove);
                remaining -= toRemove;
            }
        }

        for (PhysicStore store : storeRepository.findAll()) {
            if (remaining == 0) break;

            int available = getQuantityFromHolder(store, book);
            int toRemove = Math.min(available, remaining);
            if (toRemove > 0) {
                storeService.removeBook(store.getId(), book, toRemove);
                remaining -= toRemove;
            }
        }

        int removed = quantity - remaining;
        if (removed <= 0) return;

        List<PhysicStore> stores = storeRepository.findAll();
        if (!stores.isEmpty()) {
            int perStore = removed / stores.size();
            int leftover = removed % stores.size();

            for (PhysicStore store : stores) {
                int toAdd = perStore + (leftover-- > 0 ? 1 : 0);
                if (toAdd > 0) {
                    storeService.addBook(store.getId(), book, toAdd);
                }
            }
        }
    }

    private int getQuantityFromHolder(InventoryHolder holder, Book book) {
        return holder.getInventoryItems().stream()
                .filter(i -> i.getBook().equals(book))
                .mapToInt(InventoryItem::getQuantity)
                .sum();
    }

    private void validateBook(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");
        if (book.getId() == null) {
            throw new IllegalArgumentException("Book ID cannot be null");
        }
    }
}
