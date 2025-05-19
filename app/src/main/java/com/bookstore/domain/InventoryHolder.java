package com.bookstore.domain;

import com.bookstore.domain.inventoryconfig.InventoryConfig;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InventoryHolder extends InventoryConfig {
    protected final Map<Book, Integer> inventory = new ConcurrentHashMap<>();

    public void addItem(Book book, int quantity) {
        validateAddition(book, quantity);
        inventory.merge(book, quantity, Integer::sum);
    }

    public void removeItem(Book book, int quantity) {
        validateRemoval(book, quantity);
        inventory.computeIfPresent(book, (k, v) -> v - quantity);
    }

    public boolean hasItem(Book book) {
        return inventory.containsKey(book) && inventory.get(book) > 0;
    }

    public int getItemQuantity(Book book) {
        return inventory.getOrDefault(book, 0);
    }

    public Map<Book, Integer> getInventory() {
        return inventory;
    }

    protected void validateAddition(Book book, int quantity) {
        Objects.requireNonNull(book, "Book cannot be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }

    protected void validateRemoval(Book book, int quantity) {
        validateAddition(book, quantity);
        if (!inventory.containsKey(book)) {
            throw new IllegalArgumentException("Item not found in inventory");
        }
        if (inventory.get(book) < quantity) {
            throw new IllegalStateException("Insufficient quantity");
        }
    }
}
