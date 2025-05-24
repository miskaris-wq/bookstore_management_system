package com.bookstore.domain;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "inventory_items")
public class InventoryItem {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "inventory_holder_id", nullable = false)
    private InventoryHolder inventoryHolder;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public InventoryItem() {}

    public InventoryItem(Book book, InventoryHolder holder, int quantity) {
        this.book = Objects.requireNonNull(book);
        this.inventoryHolder = Objects.requireNonNull(holder);
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public InventoryHolder getInventoryHolder() {
        return inventoryHolder;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setInventoryHolder(InventoryHolder inventoryHolder) {
        this.inventoryHolder = inventoryHolder;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        this.quantity = quantity;
    }



}
