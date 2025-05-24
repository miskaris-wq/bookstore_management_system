package com.bookstore.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "publication_year", nullable = false)
    private int publicationYear;

    public Book() {
    }

    public Book(UUID id, String title, String author, String genre,
                BigDecimal price, int publicationYear) {
        this.id = Objects.requireNonNull(id, "Book ID cannot be null");
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
            throw new IllegalArgumentException("Title cannot be empty or null");
        }
        return title.trim().toLowerCase();
    }

    private String validateAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty or null");
        }
        return author.trim().toLowerCase();
    }

    private String validateGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be empty or null");
        }
        return genre.trim().toLowerCase();
    }

    private BigDecimal validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        return price;
    }

    private int validateYear(int year) {
        int currentYear = java.time.Year.now().getValue();
        if (year < 0 || year > currentYear) {
            throw new IllegalArgumentException("Incorrect year of publication");
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

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return getPublicationYear() == book.getPublicationYear() && Objects.equals(getId(), book.getId()) && Objects.equals(getTitle(), book.getTitle()) && Objects.equals(getAuthor(), book.getAuthor()) && Objects.equals(getGenre(), book.getGenre()) && Objects.equals(getPrice(), book.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getAuthor(), getGenre(), getPrice(), getPublicationYear());
    }

    @Override
    public String toString() {
        return String.format("Book [ID: %s, Title: %s, Author: %s, Genre: %s, Price: %.2f, Year: %d]",
                id, title, author, genre, price, publicationYear);
    }
}
