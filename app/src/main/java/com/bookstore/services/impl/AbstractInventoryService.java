package com.bookstore.services.impl;

import com.bookstore.domain.Book;
import com.bookstore.domain.InventoryHolder;
import com.bookstore.domain.InventoryItem;
import com.bookstore.exceptions.InsufficientStockException;
import com.bookstore.services.api.InventoryOperations;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractInventoryService implements InventoryOperations {

    protected abstract Optional<? extends InventoryHolder> findInventoryHolderById(UUID locationId);
    protected abstract void saveInventoryHolder(InventoryHolder holder);
    protected abstract void validateLocation(UUID locationId);

    @Override
    public void addBook(UUID locationId, Book book, int quantity) {
        if (book == null) throw new NullPointerException("Book must not be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        validateLocation(locationId);

        InventoryHolder holder = findInventoryHolderById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found: " + locationId));

        Optional<InventoryItem> itemOpt = holder.getInventoryItems().stream()
                .filter(i -> i.getBook().equals(book))
                .findFirst();

        if (itemOpt.isPresent()) {
            InventoryItem item = itemOpt.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            InventoryItem newItem = new InventoryItem(book, holder, quantity);
            holder.getInventoryItems().add(newItem);
        }

        saveInventoryHolder(holder);
    }

    @Override
    public void removeBook(UUID locationId, Book book, int quantity) throws InsufficientStockException {
        validateLocation(locationId);

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        InventoryHolder holder = findInventoryHolderById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found: " + locationId));

        Optional<InventoryItem> itemOpt = holder.getInventoryItems().stream()
                .filter(i -> i.getBook().equals(book))
                .findFirst();

        if (itemOpt.isEmpty()) {
            throw new InsufficientStockException(book.getTitle(), 0, quantity);
        }

        InventoryItem item = itemOpt.get();
        int currentQuantity = item.getQuantity();

        if (currentQuantity < quantity) {
            throw new InsufficientStockException(book.getTitle(), currentQuantity, quantity);
        }

        item.setQuantity(currentQuantity - quantity);

        if (item.getQuantity() == 0) {
            holder.getInventoryItems().remove(item);
        }

        saveInventoryHolder(holder);
    }

    @Override
    public Map<Book, Integer> getInventory(UUID locationId) {
        validateLocation(locationId);

        InventoryHolder holder = findInventoryHolderById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found: " + locationId));

        // Формируем Map<Book, Integer> из InventoryItem
        return holder.getInventoryItems().stream()
                .collect(Collectors.toMap(
                        InventoryItem::getBook,
                        InventoryItem::getQuantity
                ));
    }

    @Override
    public List<Book> getLowStockBooks(UUID locationId, int threshold) {
        validateLocation(locationId);

        InventoryHolder holder = findInventoryHolderById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found: " + locationId));

        return holder.getInventoryItems().stream()
                .filter(i -> i.getQuantity() < threshold)
                .map(InventoryItem::getBook)
                .collect(Collectors.toList());
    }
}
