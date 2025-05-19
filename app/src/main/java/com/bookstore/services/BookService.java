package com.bookstore.services;

import com.bookstore.dao.api.BookRepository;
import com.bookstore.dao.impl.utils.impl.BookFinder;
import com.bookstore.domain.Book;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public void addBook(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");
        bookRepository.add(book);
    }
    @PreAuthorize("permitAll()")
    public Optional<Book> findBookById(UUID id) {
        Objects.requireNonNull(id, "ID cannot be null");
        List<Book> books = bookRepository.findById(id);
        return books.isEmpty() ? Optional.empty() : Optional.of(books.getFirst());
    }
    @PreAuthorize("permitAll()")
    public Map<String, List<Book>> groupBooksByGenre() {
        return bookRepository.findAll().stream()
                .collect(Collectors.groupingBy(Book::getGenre));
    }
    @PreAuthorize("permitAll()")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    @PreAuthorize("hasRole('ADMIN')")
    public boolean removeBook(UUID id) {
        Objects.requireNonNull(id, "ID cannot be null");
        return bookRepository.deleteById(id);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public BookFinder createBookFinder() {
        return bookRepository.newFinder();
    }
}
