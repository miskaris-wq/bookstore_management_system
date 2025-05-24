package com.bookstore.dto;

import java.util.UUID;

public class InventoryItemDTO {
    private UUID id;
    private BookDTO book;
    private Object inventoryHolder;
    private int quantity;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    public Object getInventoryHolder() {
        return inventoryHolder;
    }

    public void setInventoryHolder(Object inventoryHolder) {
        this.inventoryHolder = inventoryHolder;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
