package com.bookstore.datasourse;

import com.bookstore.domain.Book;
import com.bookstore.domain.Order;
import com.bookstore.domain.PhysicStore;
import com.bookstore.domain.Warehouse;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DataSource {
    private static final Map<UUID, Book> books = new ConcurrentHashMap<>();
    private static final Map<UUID, Warehouse> warehouses = new ConcurrentHashMap<>();
    private static final Map<UUID, PhysicStore> stores = new ConcurrentHashMap<>();
    private static final Map<UUID, Order> orders = new ConcurrentHashMap<>();

    public DataSource() {
    }

    public static Map<UUID, Book> getBooks() {
        return Collections.unmodifiableMap(books);
    }

    public static Map<UUID, Warehouse> getWarehouses() {
        return Collections.unmodifiableMap(warehouses);
    }

    public static Map<UUID, PhysicStore> getStores() {
        return Collections.unmodifiableMap(stores);
    }

    public static Map<UUID, Order> getOrders() {
        return Collections.unmodifiableMap(orders);
    }

    public static void addBook(UUID id, Book book) {
        books.put(id, book);
    }

    public static void addWarehouse(UUID id, Warehouse warehouse) {
        warehouses.put(id, warehouse);
    }

    public static void addStore(UUID id, PhysicStore store) {
        stores.put(id, store);
    }

    public static void addOrder(UUID id, Order order) {
        orders.put(id, order);
    }

    public static boolean deleteBook(UUID id) {
        return books.remove(id) != null;
    }

    public static boolean deleteOrder(UUID id) {
        return orders.remove(id)  != null ;
    }

    public static boolean deleteStore(UUID id) {
        return stores.remove(id)  != null ;
    }

    public static boolean deleteWarehouse(UUID id) {
        return warehouses.remove(id)  != null ;
    }

}
