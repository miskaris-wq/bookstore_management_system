package com.bookstore.dto;

import java.math.BigDecimal;

public class OrderItemDTO {
    private BookDTO book;
    private int quantity;
    private BigDecimal itemTotal;

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
