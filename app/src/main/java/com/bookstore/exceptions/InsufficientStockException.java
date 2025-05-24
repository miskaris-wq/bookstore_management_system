package com.bookstore.exceptions;

public class InsufficientStockException extends WarehouseException {
    private final String title;
    private final int available;
    private final int requested;

    public InsufficientStockException(String title, int available, int requested) {
        super(String.format(
                "Not enough stock. Book: %s, Available: %d, Requested: %d",
                title, available, requested
        ));
        this.title = title;
        this.available = available;
        this.requested = requested;
    }

    public String getBookTitle() { return title; }
    public int getAvailable() { return available; }
    public int getRequested() { return requested; }
}
