package com.bookstore.services;

import com.bookstore.domain.Book;
import com.bookstore.domain.InventoryHolder;
import com.bookstore.exceptions.InsufficientStockException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class AbstractInventoryService implements InventoryOperations {

    protected final InventoryHolder inventoryHolder;

    protected AbstractInventoryService(InventoryHolder inventoryHolder) {
        this.inventoryHolder = inventoryHolder;
    }

    @Override
    public void addBook(UUID locationId, Book book, int quantity) {
        validateLocation(locationId);
        inventoryHolder.addItem(book, quantity);
    }

    @Override
    public void removeBook(UUID locationId, Book book, int quantity) throws InsufficientStockException {
        validateLocation(locationId);
        try {
            inventoryHolder.removeItem(book, quantity);
        } catch (IllegalStateException e) {
            throw new InsufficientStockException(
                    book.getTitle(),
                    inventoryHolder.getItemQuantity(book),
                    quantity
            );
        }
    }

    @Override
    public Map<Book, Integer> getInventory(UUID locationId) {
        validateLocation(locationId);
        return inventoryHolder.getInventory();
    }

    @Override
    public List<Book> getLowStockBooks(UUID locationId, int threshold) {
        validateLocation(locationId);
        return inventoryHolder.getInventory().entrySet().stream()
                .filter(entry -> entry.getValue() < threshold)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    protected abstract void validateLocation(UUID locationId);
}
