package com.bookstore.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderItem {
    private final Book book;     // Книга в заказе
    private final int quantity;  // Количество экземпляров

    public OrderItem(Book book, int quantity) {
        this.book = Objects.requireNonNull(book, "Книга не может быть null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть положительным");
        }
        this.quantity = quantity;
    }

    public Book getBook() { return book; }
    public int getQuantity() { return quantity; }

    public BigDecimal getItemTotal() {
        return book.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public String toString() {
        return String.format("%s x%d = %.2f руб.",
                book.getTitle(), quantity, getItemTotal());
    }
}
