package com.bookstore.services;

import com.bookstore.dao.api.PhysicStoreRepository;
import com.bookstore.dao.api.WarehouseRepository;
import com.bookstore.dao.api.BookRepository;
import com.bookstore.domain.Book;
import com.bookstore.domain.InventoryHolder;
import com.bookstore.domain.PhysicStore;
import com.bookstore.domain.Warehouse;
import com.bookstore.exceptions.InsufficientStockException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    private final WarehouseRepository warehouseRepository;
    private final PhysicStoreRepository storeRepository;
    private final BookRepository bookRepository;
    private final InventoryHolder warehouseInventory;
    private final InventoryHolder storeInventory;

    public InventoryService(WarehouseRepository warehouseRepository,
                            PhysicStoreRepository storeRepository,
                            BookRepository bookRepository,
                            @Qualifier("warehouseInventory") InventoryHolder warehouseInventory,
                            @Qualifier("storeInventory") InventoryHolder storeInventory) {
        this.warehouseRepository = warehouseRepository;
        this.storeRepository = storeRepository;
        this.bookRepository = bookRepository;
        this.warehouseInventory = warehouseInventory;
        this.storeInventory = storeInventory;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_MANAGER', 'MANAGER')")
    public int getTotalStock(Book book) {
        int warehouseStock = warehouseRepository.findAll().stream()
                .mapToInt(w -> w.getInventory().getOrDefault(book, 0))
                .sum();

        int storeStock = storeRepository.findAll().stream()
                .mapToInt(s -> s.getInventory().getOrDefault(book, 0))
                .sum();

        return warehouseStock + storeStock;
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void redistributeStock(Book book, int quantity)
            throws InsufficientStockException {
        // 1. Проверяем существование книги
        if (book == null || bookRepository.findById(book.getId()).isEmpty()) {
            throw new IllegalArgumentException("Book does not exist");
        }

        // 2. Проверяем доступное количество
        int totalAvailable = getTotalQuantity(book);
        if (totalAvailable < quantity) {
            throw new InsufficientStockException(
                    book.getTitle(), totalAvailable, quantity);
        }

        // 3. Собираем книги со складов через InventoryHolder
        int remaining = quantity;
        for (Warehouse warehouse : warehouseRepository.findAll()) {
            if (remaining <= 0) break;

            int available = warehouseInventory.getItemQuantity(book);
            if (available > 0) {
                int transfer = Math.min(available, remaining);
                try {
                    warehouseInventory.removeItem(book, transfer);
                    remaining -= transfer;
                } catch (IllegalStateException e) {
                    throw new InsufficientStockException(
                            book.getTitle(), available, transfer);
                }
            }
        }

        // 4. Если нужно, собираем книги из магазинов через InventoryHolder
        for (PhysicStore store : storeRepository.findAll()) {
            if (remaining <= 0) break;

            int available = storeInventory.getItemQuantity(book);
            if (available > 0) {
                int transfer = Math.min(available, remaining);
                try {
                    storeInventory.removeItem(book, transfer);
                    remaining -= transfer;
                } catch (IllegalStateException e) {
                    throw new InsufficientStockException(
                            book.getTitle(), available, transfer);
                }
            }
        }

        // 5. Распределяем собранные книги по магазинам
        if (quantity > 0) {
            List<PhysicStore> stores = storeRepository.findAll();
            if (!stores.isEmpty()) {
                int perStore = quantity / stores.size();
                int remainder = quantity % stores.size();

                for (PhysicStore store : stores) {
                    int amount = perStore + (remainder-- > 0 ? 1 : 0);
                    if (amount > 0) {
                        storeInventory.addItem(book, amount);
                    }
                }
            }
        }
    }

    private int getTotalQuantity(Book book) {
        int warehouseTotal = warehouseRepository.findAll().stream()
                .mapToInt(w -> warehouseInventory.getItemQuantity(book))
                .sum();

        int storeTotal = storeRepository.findAll().stream()
                .mapToInt(s -> storeInventory.getItemQuantity(book))
                .sum();

        return warehouseTotal + storeTotal;
    }
}
