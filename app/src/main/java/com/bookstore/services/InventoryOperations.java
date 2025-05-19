package com.bookstore.services;

import com.bookstore.domain.Book;
import com.bookstore.exceptions.InsufficientStockException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface InventoryOperations {
    void addBook(UUID locationId, Book book, int quantity);
    void removeBook(UUID locationId, Book book, int quantity) throws InsufficientStockException;
    Map<Book, Integer> getInventory(UUID locationId);
    List<Book> getLowStockBooks(UUID locationId, int threshold);
}



