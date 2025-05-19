package com.bookstore.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Book {
    private final UUID id;            // Уникальный идентификатор книги
    private final String title;        // Название книги
    private final String author;       // Автор книги
    private final String genre;        // Жанр книги
    private final BigDecimal price;       // Цена книги
    private final int publicationYear; // Год издания

    public Book(UUID id, String title, String author, String genre,
                BigDecimal price, int publicationYear) {
        this.id = Objects.requireNonNull(id, "ID книги не может быть null");
        this.title = validateTitle(title);
        this.author = validateAuthor(author);
        this.genre = validateGenre(genre);
        this.price = validatePrice(price);
        this.publicationYear = validateYear(publicationYear);
    }

    public Book(String title, String author, String genre,
                BigDecimal price, int publicationYear) {
        this(UUID.randomUUID(), title, author, genre, price, publicationYear);
    }

    private String validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Название книги не может быть пустым");
        }
        return title.trim().toLowerCase();
    }

    private String validateAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Автор не может быть пустым");
        }
        return author.trim().toLowerCase();
    }

    private String validateGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Жанр не может быть пустым");
        }
        return genre.trim().toLowerCase();
    }

    private BigDecimal validatePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("Цена должна быть положительной");
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Цена должна быть положительной");
        }
        return price;
    }

    private int validateYear(int year) {
        int currentYear = java.time.Year.now().getValue();
        if (year < 0 || year > currentYear) {
            throw new IllegalArgumentException(
                    String.format("Год издания должен быть между 0 и %d", currentYear));
        }
        return year;
    }


    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Книга [ID: %s, Название: %s, Автор: %s, Жанр: %s, Цена: %.2f, Год: %d]",
                id, title, author, genre, price, publicationYear);
    }

}
